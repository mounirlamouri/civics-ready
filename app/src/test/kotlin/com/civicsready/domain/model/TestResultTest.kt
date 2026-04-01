package com.civicsready.domain.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class TestResultTest {

    private fun fakeQuestion(id: Int = 1) = CivicsQuestion(
        id = id,
        section = Section.PRINCIPLES_OF_GOVERNMENT,
        text = "Question $id",
        acceptableAnswers = listOf("Answer $id")
    )

    private fun result(correct: Boolean, id: Int = 1) =
        QuestionResult(question = fakeQuestion(id), wasCorrect = correct)

    // ── Standard mode (need 12 / 20) ─────────────────────────────────────────

    @Test
    fun `passingThreshold is 12 in standard mode`() {
        val testResult = TestResult(questionResults = emptyList(), is6520Mode = false)
        assertEquals(12, testResult.passingThreshold)
    }

    @Test
    fun `passes standard mode when correct count equals 12`() {
        val results = (1..20).map { result(correct = it <= 12, id = it) }
        val testResult = TestResult(results, is6520Mode = false)
        assertEquals(12, testResult.correctCount)
        assertTrue(testResult.passed)
    }

    @Test
    fun `fails standard mode when correct count is 11`() {
        val results = (1..20).map { result(correct = it <= 11, id = it) }
        val testResult = TestResult(results, is6520Mode = false)
        assertEquals(11, testResult.correctCount)
        assertFalse(testResult.passed)
    }

    @Test
    fun `passes standard mode with perfect score`() {
        val results = (1..20).map { result(correct = true, id = it) }
        val testResult = TestResult(results, is6520Mode = false)
        assertEquals(20, testResult.correctCount)
        assertTrue(testResult.passed)
    }

    // ── 65/20 mode (need 6 / 10) ─────────────────────────────────────────────

    @Test
    fun `passingThreshold is 6 in 6520 mode`() {
        val testResult = TestResult(questionResults = emptyList(), is6520Mode = true)
        assertEquals(6, testResult.passingThreshold)
    }

    @Test
    fun `passes 6520 mode when correct count equals 6`() {
        val results = (1..10).map { result(correct = it <= 6, id = it) }
        val testResult = TestResult(results, is6520Mode = true)
        assertEquals(6, testResult.correctCount)
        assertTrue(testResult.passed)
    }

    @Test
    fun `fails 6520 mode when correct count is 5`() {
        val results = (1..10).map { result(correct = it <= 5, id = it) }
        val testResult = TestResult(results, is6520Mode = true)
        assertEquals(5, testResult.correctCount)
        assertFalse(testResult.passed)
    }

    // ── Derived properties ────────────────────────────────────────────────────

    @Test
    fun `totalQuestions reflects the question results list size`() {
        val results = (1..20).map { result(correct = true, id = it) }
        val testResult = TestResult(results, is6520Mode = false)
        assertEquals(20, testResult.totalQuestions)
    }

    @Test
    fun `correctCount counts only wasCorrect results`() {
        val results = listOf(
            result(correct = true, id = 1),
            result(correct = false, id = 2),
            result(correct = true, id = 3),
            result(correct = false, id = 4),
            result(correct = true, id = 5)
        )
        val testResult = TestResult(results, is6520Mode = false)
        assertEquals(3, testResult.correctCount)
    }

    @Test
    fun `correctCount is zero when all wrong`() {
        val results = (1..20).map { result(correct = false, id = it) }
        val testResult = TestResult(results, is6520Mode = false)
        assertEquals(0, testResult.correctCount)
        assertFalse(testResult.passed)
    }

    @Test
    fun `empty results list means zero correct and not passed`() {
        val testResult = TestResult(emptyList(), is6520Mode = false)
        assertEquals(0, testResult.totalQuestions)
        assertEquals(0, testResult.correctCount)
        assertFalse(testResult.passed)
    }
}
