package com.civicsready.ui.test

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.civicsready.data.repository.CivicsRepository
import com.civicsready.domain.model.CivicsQuestion
import com.civicsready.domain.model.QuestionResult
import com.civicsready.domain.model.TestResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

data class TestUiState(
    val questions: List<CivicsQuestion> = emptyList(),
    val currentIndex: Int = 0,
    val results: List<QuestionResult> = emptyList(),
    val isFinished: Boolean = false,
    val isLoading: Boolean = true,
    val is6520Mode: Boolean = false,
    val error: String? = null
) {
    val currentQuestion: CivicsQuestion? get() = questions.getOrNull(currentIndex)
    val totalQuestions: Int get() = questions.size
    val testResult: TestResult get() = TestResult(results, is6520Mode)
}

@HiltViewModel
class TestViewModel @Inject constructor(
    private val repository: CivicsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TestUiState())
    val uiState: StateFlow<TestUiState> = _uiState.asStateFlow()

    init {
        loadQuestions()
    }

    private fun loadQuestions() {
        _uiState.value = TestUiState()
        viewModelScope.launch {
            try {
                val is6520   = repository.is6520Mode.first()
                val isOrdered = repository.isOrderedMode.first()
                val pool     = repository.getQuestions(for6520Only = is6520).let {
                    if (isOrdered) it else it.shuffled()
                }
                val count     = if (is6520) 10 else 20
                val questions = pool.take(count)
                _uiState.value = TestUiState(
                    questions  = questions,
                    is6520Mode = is6520,
                    isLoading  = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error     = "Failed to load questions. Please try again."
                )
            }
        }
    }

    fun onCorrect() = recordAndAdvance(correct = true)
    fun onIncorrect() = recordAndAdvance(correct = false)

    private fun recordAndAdvance(correct: Boolean) {
        val state    = _uiState.value
        val question = state.currentQuestion ?: return
        viewModelScope.launch {
            repository.recordAnswer(question.id, correct)
        }
        val newResults = state.results + QuestionResult(question, correct)
        val nextIndex  = state.currentIndex + 1
        _uiState.value = state.copy(
            results      = newResults,
            currentIndex = nextIndex,
            isFinished   = nextIndex >= state.totalQuestions
        )
    }

    fun restart() = loadQuestions()
}
