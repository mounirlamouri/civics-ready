package com.civicsready.ui.practice

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.civicsready.data.repository.CivicsRepository
import com.civicsready.domain.model.CivicsQuestion
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PracticeUiState(
    val questions: List<CivicsQuestion> = emptyList(),
    val currentIndex: Int = 0,
    val correctCount: Int = 0,
    val incorrectCount: Int = 0,
    val isFinished: Boolean = false,
    val isLoading: Boolean = true,
    val error: String? = null
) {
    val currentQuestion: CivicsQuestion? get() = questions.getOrNull(currentIndex)
    val totalQuestions: Int get() = questions.size
    val answeredCount: Int get() = correctCount + incorrectCount
}

@HiltViewModel
class PracticeViewModel @Inject constructor(
    private val repository: CivicsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PracticeUiState())
    val uiState: StateFlow<PracticeUiState> = _uiState.asStateFlow()

    init {
        loadQuestions()
    }

    private fun loadQuestions() {
        _uiState.value = PracticeUiState()
        viewModelScope.launch {
            try {
                val is6520    = repository.is6520Mode.first()
                val isOrdered = repository.isOrderedMode.first()
                val questions = repository.getQuestions(for6520Only = is6520).let {
                    if (isOrdered) it else it.shuffled()
                }
                _uiState.value = PracticeUiState(questions = questions, isLoading = false)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error     = "Failed to load questions. Please try again."
                )
            }
        }
    }

    fun onCorrect() {
        val state = _uiState.value
        val question = state.currentQuestion ?: return
        viewModelScope.launch {
            repository.recordAnswer(question.id, correct = true)
        }
        advance(correct = true)
    }

    fun onIncorrect() {
        val state = _uiState.value
        val question = state.currentQuestion ?: return
        viewModelScope.launch {
            repository.recordAnswer(question.id, correct = false)
        }
        advance(correct = false)
    }

    private fun advance(correct: Boolean) {
        val state = _uiState.value
        val newCorrect   = state.correctCount   + if (correct) 1 else 0
        val newIncorrect = state.incorrectCount + if (correct) 0 else 1
        val nextIndex    = state.currentIndex + 1
        _uiState.value = state.copy(
            currentIndex  = nextIndex,
            correctCount  = newCorrect,
            incorrectCount = newIncorrect,
            isFinished    = nextIndex >= state.totalQuestions
        )
    }

    fun finish() {
        _uiState.value = _uiState.value.copy(isFinished = true)
    }

    fun restart() = loadQuestions()
}
