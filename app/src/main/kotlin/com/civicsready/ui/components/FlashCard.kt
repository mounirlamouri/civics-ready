package com.civicsready.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.civicsready.R
import com.civicsready.domain.model.CivicsQuestion
import com.civicsready.ui.theme.GreenPass
import com.civicsready.ui.theme.White

@Composable
fun FlashCard(
    question: CivicsQuestion,
    questionNumber: Int,
    totalQuestions: Int,
    onCorrect: () -> Unit,
    onIncorrect: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showAnswer by remember(question.id) { mutableStateOf(false) }
    val rotation by animateFloatAsState(
        targetValue = if (showAnswer) 180f else 0f,
        animationSpec = tween(durationMillis = 400),
        label = "card_flip"
    )

    // Swipe gesture state
    var dragX by remember { mutableFloatStateOf(0f) }
    val swipeThreshold = 120f

    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {

        // Question counter
        Text(
            text  = stringResource(R.string.flashcard_question_counter, questionNumber, totalQuestions),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Card container with flip + swipe
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .pointerInput(question.id, showAnswer) {
                    detectHorizontalDragGestures(
                        onDragEnd = {
                            if (showAnswer) {
                                when {
                                    dragX > swipeThreshold  -> onCorrect()
                                    dragX < -swipeThreshold -> onIncorrect()
                                }
                            }
                            dragX = 0f
                        },
                        onHorizontalDrag = { _, delta -> dragX += delta }
                    )
                },
            contentAlignment = Alignment.Center
        ) {
            // Front face (question)
            if (rotation <= 90f) {
                CardFace(
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer { rotationY = rotation },
                    backgroundColor = MaterialTheme.colorScheme.surface,
                    onClick = { showAnswer = true }
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text  = question.section.displayName,
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            text      = question.text,
                            style     = MaterialTheme.typography.headlineMedium,
                            color     = MaterialTheme.colorScheme.onSurface,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            text  = stringResource(R.string.flashcard_tap_reveal),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Back face (answer)
            if (rotation > 90f) {
                CardFace(
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer { rotationY = rotation - 180f },
                    backgroundColor = MaterialTheme.colorScheme.surfaceVariant,
                    onClick = {}
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        val minAnswers = question.minimumAnswersRequired
                        val answerLabel = when {
                            minAnswers > 1 -> stringResource(R.string.flashcard_answer_name_n, minAnswers)
                            question.acceptableAnswers.size > 1 -> stringResource(R.string.flashcard_answer_any)
                            else -> stringResource(R.string.flashcard_answer_label)
                        }
                        Text(
                            text  = answerLabel,
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .verticalScroll(rememberScrollState()),
                            horizontalAlignment = Alignment.Start
                        ) {
                            question.acceptableAnswers.forEach { answer ->
                                Text(
                                    text  = "• $answer",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(vertical = 2.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text  = stringResource(R.string.flashcard_did_you_get_it),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row {
                            Button(
                                onClick = onCorrect,
                                colors  = ButtonDefaults.buttonColors(containerColor = GreenPass),
                                shape   = RoundedCornerShape(12.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(stringResource(R.string.flashcard_got_it), color = White)
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            OutlinedButton(
                                onClick = onIncorrect,
                                shape   = RoundedCornerShape(12.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(stringResource(R.string.flashcard_missed), color = MaterialTheme.colorScheme.error)
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text  = stringResource(R.string.flashcard_swipe_hint),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CardFace(
    modifier: Modifier = Modifier,
    backgroundColor: androidx.compose.ui.graphics.Color,
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    Card(
        modifier  = modifier,
        shape     = RoundedCornerShape(20.dp),
        colors    = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        onClick   = onClick
    ) {
        content()
    }
}
