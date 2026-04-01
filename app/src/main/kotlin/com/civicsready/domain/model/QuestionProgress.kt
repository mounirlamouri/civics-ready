package com.civicsready.domain.model

data class QuestionProgress(
    val questionId: Int,
    val totalAttempts: Int = 0,
    val correctAttempts: Int = 0,
    val lastAttemptTimestamp: Long = 0L
) {
    val successRate: Float
        get() = if (totalAttempts == 0) 0f else (correctAttempts.toFloat() / totalAttempts).coerceIn(0f, 1f)
}
