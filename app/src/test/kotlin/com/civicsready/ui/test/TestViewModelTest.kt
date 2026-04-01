@file:OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)

package com.civicsready.ui.test

import com.civicsready.data.repository.CivicsRepository
import com.civicsready.domain.model.ALL_QUESTIONS
import com.civicsready.domain.model.QUESTIONS_6520
import com.civicsready.domain.model.CivicsQuestion
import com.civicsready.domain.model.Section
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

class TestViewModelTest {

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

    private fun buildVm() = TestViewModel(repository)

    // ── Initial load ──────────────────────────────────────────────────────────

    @Test
    fun `loads 20 questions in standard mode`() = runTest {
        val vm = buildVm()
        advanceUntilIdle()
        assertEquals(20, vm.uiState.value.totalQuestions)
        assertFalse(vm.uiState.value.isLoading)
    }

    @Test
    fun `loads 10 questions in 6520 mode`() = runTest {
        every { repository.is6520Mode } returns flowOf(true)
        coEvery { repository.getQuestions(for6520Only = true) } returns QUESTIONS_6520

        val vm = buildVm()
        advanceUntilIdle()
        assertEquals(10, vm.uiState.value.totalQuestions)
        assertTrue(vm.uiState.value.is6520Mode)
    }

    @Test
    fun `starts at question index 0`() = runTest {
        val vm = buildVm()
        advanceUntilIdle()
        assertEquals(0, vm.uiState.value.currentIndex)
    }

    @Test
    fun `starts with empty results`() = runTest {
        val vm = buildVm()
        advanceUntilIdle()
        assertTrue(vm.uiState.value.results.isEmpty())
    }

    @Test
    fun `isFinished is false initially`() = runTest {
        val vm = buildVm()
        advanceUntilIdle()
        assertFalse(vm.uiState.value.isFinished)
    }

    @Test
    fun `currentQuestion is the first question initially`() = runTest {
        val vm = buildVm()
        advanceUntilIdle()
        assertNotNull(vm.uiState.value.currentQuestion)
        assertEquals(vm.uiState.value.questions.first(), vm.uiState.value.currentQuestion)
    }

    // ── onCorrect / onIncorrect ───────────────────────────────────────────────

    @Test
    fun `onCorrect advances to next question`() = runTest {
        val vm = buildVm()
        advanceUntilIdle()
        vm.onCorrect()
        assertEquals(1, vm.uiState.value.currentIndex)
    }

    @Test
    fun `onCorrect adds correct result`() = runTest {
        val vm = buildVm()
        advanceUntilIdle()
        vm.onCorrect()
        assertEquals(1, vm.uiState.value.results.size)
        assertTrue(vm.uiState.value.results.first().wasCorrect)
    }

    @Test
    fun `onIncorrect advances to next question`() = runTest {
        val vm = buildVm()
        advanceUntilIdle()
        vm.onIncorrect()
        assertEquals(1, vm.uiState.value.currentIndex)
    }

    @Test
    fun `onIncorrect adds incorrect result`() = runTest {
        val vm = buildVm()
        advanceUntilIdle()
        vm.onIncorrect()
        assertEquals(1, vm.uiState.value.results.size)
        assertFalse(vm.uiState.value.results.first().wasCorrect)
    }

    @Test
    fun `onCorrect calls repository recordAnswer with correct=true`() = runTest {
        val vm = buildVm()
        advanceUntilIdle()
        val firstQuestion = vm.uiState.value.currentQuestion!!
        vm.onCorrect()
        advanceUntilIdle()
        coVerify { repository.recordAnswer(firstQuestion.id, true) }
    }

    @Test
    fun `onIncorrect calls repository recordAnswer with correct=false`() = runTest {
        val vm = buildVm()
        advanceUntilIdle()
        val firstQuestion = vm.uiState.value.currentQuestion!!
        vm.onIncorrect()
        advanceUntilIdle()
        coVerify { repository.recordAnswer(firstQuestion.id, false) }
    }

    // ── Finishing the test ────────────────────────────────────────────────────

    @Test
    fun `isFinished becomes true after answering all 20 questions`() = runTest {
        val vm = buildVm()
        advanceUntilIdle()
        repeat(20) { vm.onCorrect() }
        assertTrue(vm.uiState.value.isFinished)
    }

    @Test
    fun `currentQuestion is null when finished`() = runTest {
        val vm = buildVm()
        advanceUntilIdle()
        repeat(20) { vm.onCorrect() }
        assertNull(vm.uiState.value.currentQuestion)
    }

    @Test
    fun `testResult reflects mixed correct and incorrect answers`() = runTest {
        val vm = buildVm()
        advanceUntilIdle()
        repeat(12) { vm.onCorrect() }
        repeat(8) { vm.onIncorrect() }
        val result = vm.uiState.value.testResult
        assertEquals(12, result.correctCount)
        assertEquals(20, result.totalQuestions)
        assertTrue(result.passed)
    }

    @Test
    fun `testResult fails when fewer than 12 correct`() = runTest {
        val vm = buildVm()
        advanceUntilIdle()
        repeat(11) { vm.onCorrect() }
        repeat(9) { vm.onIncorrect() }
        val result = vm.uiState.value.testResult
        assertEquals(11, result.correctCount)
        assertFalse(result.passed)
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
        assertEquals(20, vm.uiState.value.totalQuestions)
    }

    // ── restart ───────────────────────────────────────────────────────────────

    @Test
    fun `restart reloads questions and resets state`() = runTest {
        val vm = buildVm()
        advanceUntilIdle()
        repeat(5) { vm.onCorrect() }
        vm.restart()
        advanceUntilIdle()
        assertEquals(0, vm.uiState.value.currentIndex)
        assertTrue(vm.uiState.value.results.isEmpty())
        assertFalse(vm.uiState.value.isFinished)
        assertEquals(20, vm.uiState.value.totalQuestions)
    }
}
