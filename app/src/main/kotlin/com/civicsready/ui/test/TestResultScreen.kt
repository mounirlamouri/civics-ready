package com.civicsready.ui.test

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.civicsready.R
import com.civicsready.domain.model.QuestionResult
import com.civicsready.ui.theme.GreenPass
import com.civicsready.ui.theme.RedFail
import com.civicsready.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TestResultScreen(
    onHome: () -> Unit,
    onRetry: () -> Unit,
    viewModel: TestViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val result = state.testResult

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.result_title), style = MaterialTheme.typography.titleLarge) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor    = if (result.passed) GreenPass else RedFail,
                    titleContentColor = White
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        LazyColumn(
            modifier              = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement  = Arrangement.spacedBy(8.dp)
        ) {
            // Score banner
            item {
                Spacer(Modifier.height(16.dp))
                Card(
                    modifier  = Modifier.fillMaxWidth(),
                    shape     = RoundedCornerShape(20.dp),
                    colors    = CardDefaults.cardColors(
                        containerColor = if (result.passed) GreenPass else RedFail
                    )
                ) {
                    Column(
                        modifier            = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text  = stringResource(if (result.passed) R.string.result_pass else R.string.result_fail),
                            style = MaterialTheme.typography.headlineLarge,
                            color = White
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text  = stringResource(R.string.result_score, result.correctCount, result.totalQuestions),
                            style = MaterialTheme.typography.titleLarge,
                            color = White
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text  = stringResource(R.string.result_threshold, result.passingThreshold),
                            style = MaterialTheme.typography.bodyMedium,
                            color = White
                        )
                    }
                }
            }

            item { Spacer(Modifier.height(8.dp)) }

            // Per-question results
            item {
                Text(
                    stringResource(R.string.result_question_by_question),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            items(result.questionResults) { qr ->
                QuestionResultRow(qr)
            }

            item {
                Spacer(Modifier.height(8.dp))
                Button(
                    onClick  = onRetry,
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape    = RoundedCornerShape(16.dp),
                    colors   = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text(stringResource(R.string.result_try_again), style = MaterialTheme.typography.titleMedium)
                }
                Spacer(Modifier.height(8.dp))
                OutlinedButton(
                    onClick  = onHome,
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape    = RoundedCornerShape(16.dp)
                ) {
                    Text(stringResource(R.string.result_back_home), color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.titleMedium)
                }
                Spacer(Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun QuestionResultRow(result: QuestionResult) {
    Card(
        modifier  = Modifier.fillMaxWidth(),
        shape     = RoundedCornerShape(12.dp),
        colors    = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Row(
            modifier      = Modifier.padding(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier        = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(if (result.wasCorrect) GreenPass else RedFail),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector        = if (result.wasCorrect) Icons.Default.CheckCircle else Icons.Default.Close,
                    contentDescription = if (result.wasCorrect) "Correct" else "Incorrect",
                    tint               = White,
                    modifier           = Modifier.size(18.dp)
                )
            }
            Column(modifier = Modifier.padding(start = 12.dp)) {
                Text(result.question.text, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface)
                if (!result.wasCorrect) {
                    Spacer(Modifier.height(4.dp))
                    val answers = result.question.acceptableAnswers
                    val answerText = when {
                        answers.size <= 2 -> answers.joinToString(", ")
                        else -> "${answers.take(2).joinToString(", ")} and ${answers.size - 2} more"
                    }
                    Text(
                        "Answer: $answerText",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.tertiary
                    )
                }
            }
        }
    }
}
