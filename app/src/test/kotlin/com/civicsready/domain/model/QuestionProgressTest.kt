package com.civicsready.domain.model

import org.junit.Assert.assertEquals
import org.junit.Test

class QuestionProgressTest {

    @Test
    fun `successRate is zero when no attempts`() {
        val progress = QuestionProgress(questionId = 1)
        assertEquals(0f, progress.successRate)
    }

    @Test
    fun `successRate is one when all attempts are correct`() {
        val progress = QuestionProgress(questionId = 1, totalAttempts = 5, correctAttempts = 5)
        assertEquals(1f, progress.successRate)
    }

    @Test
    fun `successRate is zero when no correct attempts`() {
        val progress = QuestionProgress(questionId = 1, totalAttempts = 4, correctAttempts = 0)
        assertEquals(0f, progress.successRate)
    }

    @Test
    fun `successRate is half when half correct`() {
        val progress = QuestionProgress(questionId = 1, totalAttempts = 10, correctAttempts = 5)
        assertEquals(0.5f, progress.successRate)
    }

    @Test
    fun `successRate is fractional for non-even splits`() {
        val progress = QuestionProgress(questionId = 1, totalAttempts = 3, correctAttempts = 1)
        assertEquals(1f / 3f, progress.successRate)
    }

    @Test
    fun `successRate handles single attempt correctly`() {
        val correct = QuestionProgress(questionId = 1, totalAttempts = 1, correctAttempts = 1)
        val incorrect = QuestionProgress(questionId = 2, totalAttempts = 1, correctAttempts = 0)
        assertEquals(1f, correct.successRate)
        assertEquals(0f, incorrect.successRate)
    }

    @Test
    fun `default values have zero totalAttempts and correctAttempts`() {
        val progress = QuestionProgress(questionId = 42)
        assertEquals(0, progress.totalAttempts)
        assertEquals(0, progress.correctAttempts)
        assertEquals(0L, progress.lastAttemptTimestamp)
    }
}
