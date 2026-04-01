package com.civicsready.domain.model

data class TestResult(
    val questionResults: List<QuestionResult>,
    val is6520Mode: Boolean
) {
    val totalQuestions: Int get() = questionResults.size
    val correctCount: Int get() = questionResults.count { it.wasCorrect }
    val passingThreshold: Int get() = if (is6520Mode) 6 else 12
    val passed: Boolean get() = correctCount >= passingThreshold
}

data class QuestionResult(
    val question: CivicsQuestion,
    val wasCorrect: Boolean
)
