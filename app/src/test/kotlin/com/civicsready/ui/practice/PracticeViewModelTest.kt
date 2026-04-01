@file:OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)

package com.civicsready.ui.practice

import com.civicsready.data.repository.CivicsRepository
import com.civicsready.domain.model.ALL_QUESTIONS
import com.civicsready.domain.model.QUESTIONS_6520
import com.civicsready.util.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.Runs
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class PracticeViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository: CivicsRepository = mockk()

    @Before
    fun setup() {
        every { repository.is6520Mode } returns flowOf(false)
        every { repository.isOrderedMode } returns flowOf(true) // ordered for determinism
        coEvery { repository.getQuestions(any()) } returns ALL_QUESTIONS
        coEvery { repository.recordAnswer(any(), any()) } just Runs
    }

    private fun buildVm() = PracticeViewModel(repository)

    // ── Initial load ──────────────────────────────────────────────────────────

    @Test
    fun `loads all 128 questions in standard mode`() = runTest {
        val vm = buildVm()
        advanceUntilIdle()
        assertEquals(128, vm.uiState.value.totalQuestions)
        assertFalse(vm.uiState.value.isLoading)
    }

    @Test
    fun `loads 20 questions in 6520 mode`() = runTest {
        every { repository.is6520Mode } returns flowOf(true)
        coEvery { repository.getQuestions(for6520Only = true) } returns QUESTIONS_6520

        val vm = buildVm()
        advanceUntilIdle()
        assertEquals(20, vm.uiState.value.totalQuestions)
    }

    @Test
    fun `starts at index 0 with zero counts`() = runTest {
        val vm = buildVm()
        advanceUntilIdle()
        assertEquals(0, vm.uiState.value.currentIndex)
        assertEquals(0, vm.uiState.value.correctCount)
        assertEquals(0, vm.uiState.value.incorrectCount)
    }

    @Test
    fun `isFinished is false initially`() = runTest {
        val vm = buildVm()
        advanceUntilIdle()
        assertFalse(vm.uiState.value.isFinished)
    }

    @Test
    fun `currentQuestion is first question initially`() = runTest {
        val vm = buildVm()
        advanceUntilIdle()
        assertNotNull(vm.uiState.value.currentQuestion)
        assertEquals(vm.uiState.value.questions.first(), vm.uiState.value.currentQuestion)
    }

    // ── onCorrect ─────────────────────────────────────────────────────────────

    @Test
    fun `onCorrect increments correctCount`() = runTest {
        val vm = buildVm()
        advanceUntilIdle()
        vm.onCorrect()
        assertEquals(1, vm.uiState.value.correctCount)
        assertEquals(0, vm.uiState.value.incorrectCount)
    }

    @Test
    fun `onCorrect advances currentIndex`() = runTest {
        val vm = buildVm()
        advanceUntilIdle()
        vm.onCorrect()
        assertEquals(1, vm.uiState.value.currentIndex)
    }

    @Test
    fun `onCorrect calls repository recordAnswer`() = runTest {
        val vm = buildVm()
        advanceUntilIdle()
        val firstQuestion = vm.uiState.value.currentQuestion!!
        vm.onCorrect()
        advanceUntilIdle()
        coVerify { repository.recordAnswer(firstQuestion.id, true) }
    }

    @Test
    fun `multiple onCorrect calls accumulate correctCount`() = runTest {
        val vm = buildVm()
        advanceUntilIdle()
        repeat(5) { vm.onCorrect() }
        assertEquals(5, vm.uiState.value.correctCount)
        assertEquals(0, vm.uiState.value.incorrectCount)
    }

    // ── onIncorrect ───────────────────────────────────────────────────────────

    @Test
    fun `onIncorrect increments incorrectCount`() = runTest {
        val vm = buildVm()
        advanceUntilIdle()
        vm.onIncorrect()
        assertEquals(0, vm.uiState.value.correctCount)
        assertEquals(1, vm.uiState.value.incorrectCount)
    }

    @Test
    fun `onIncorrect advances currentIndex`() = runTest {
        val vm = buildVm()
        advanceUntilIdle()
        vm.onIncorrect()
        assertEquals(1, vm.uiState.value.currentIndex)
    }

    @Test
    fun `onIncorrect calls repository recordAnswer with false`() = runTest {
        val vm = buildVm()
        advanceUntilIdle()
        val firstQuestion = vm.uiState.value.currentQuestion!!
        vm.onIncorrect()
        advanceUntilIdle()
        coVerify { repository.recordAnswer(firstQuestion.id, false) }
    }

    // ── answeredCount ─────────────────────────────────────────────────────────

    @Test
    fun `answeredCount is sum of correct and incorrect`() = runTest {
        val vm = buildVm()
        advanceUntilIdle()
        repeat(3) { vm.onCorrect() }
        repeat(2) { vm.onIncorrect() }
        assertEquals(5, vm.uiState.value.answeredCount)
    }

    // ── finish ────────────────────────────────────────────────────────────────

    @Test
    fun `finish sets isFinished to true`() = runTest {
        val vm = buildVm()
        advanceUntilIdle()
        vm.finish()
        assertTrue(vm.uiState.value.isFinished)
    }

    @Test
    fun `finish can be called mid-session`() = runTest {
        val vm = buildVm()
        advanceUntilIdle()
        repeat(10) { vm.onCorrect() }
        vm.finish()
        assertTrue(vm.uiState.value.isFinished)
        assertEquals(10, vm.uiState.value.correctCount)
    }

    // ── isFinished after all questions ────────────────────────────────────────

    @Test
    fun `isFinished becomes true after answering all 128 questions`() = runTest {
        val vm = buildVm()
        advanceUntilIdle()
        repeat(128) { vm.onCorrect() }
        assertTrue(vm.uiState.value.isFinished)
    }

    @Test
    fun `currentQuestion is null when all questions answered`() = runTest {
        val vm = buildVm()
        advanceUntilIdle()
        repeat(128) { vm.onCorrect() }
        assertNull(vm.uiState.value.currentQuestion)
    }

    // ── error state ─────────────────────────────────────────────────────────

    @Test
    fun `error state is set when repository throws`() = runTest {
        coEvery { repository.getQuestions(any()) } throws RuntimeException("DB error")

        val vm = buildVm()
        advanceUntilIdle()

        assertFalse(vm.uiState.value.isLoading)
        assertNotNull(vm.uiState.value.error)
    }

    @Test
    fun `restart clears error state on success`() = runTest {
        coEvery { repository.getQuestions(any()) } throws RuntimeException("DB error")

        val vm = buildVm()
        advanceUntilIdle()
        assertNotNull(vm.uiState.value.error)

        // Fix the mock and restart
        coEvery { repository.getQuestions(any()) } returns ALL_QUESTIONS
        vm.restart()
        advanceUntilIdle()

        assertNull(vm.uiState.value.error)
        assertEquals(128, vm.uiState.value.totalQuestions)
    }

    // ── restart ───────────────────────────────────────────────────────────────

    @Test
    fun `restart reloads questions and resets all state`() = runTest {
        val vm = buildVm()
        advanceUntilIdle()
        repeat(10) { vm.onCorrect() }
        repeat(5) { vm.onIncorrect() }
        vm.restart()
        advanceUntilIdle()
        assertEquals(0, vm.uiState.value.currentIndex)
        assertEquals(0, vm.uiState.value.correctCount)
        assertEquals(0, vm.uiState.value.incorrectCount)
        assertFalse(vm.uiState.value.isFinished)
        assertEquals(128, vm.uiState.value.totalQuestions)
    }
}
