@file:OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)

package com.civicsready.ui.home

import com.civicsready.data.repository.CivicsRepository
import com.civicsready.domain.model.Officials
import com.civicsready.domain.model.QuestionProgress
import com.civicsready.util.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.Runs
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class HomeViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository: CivicsRepository = mockk()

    @Before
    fun setup() {
        every { repository.officialsFlow } returns flowOf(Officials())
        every { repository.is6520Mode } returns flowOf(false)
        every { repository.isOrderedMode } returns flowOf(false)
        every { repository.allProgressFlow } returns flowOf(emptyMap())
        coEvery { repository.setOrderedMode(any()) } just Runs
    }

    private fun buildVm() = HomeViewModel(repository)

    // ── uiState defaults ──────────────────────────────────────────────────────

    @Test
    fun `uiState emits defaults when no progress and no officials`() = runTest {
        val state = buildVm().uiState.first()
        assertFalse(state.officials.isResolved)
        assertFalse(state.is6520Mode)
        assertFalse(state.isOrderedMode)
        assertEquals(0, state.totalAttempted)
        assertEquals(0f, state.overallAccuracy)
    }

    // ── officials ─────────────────────────────────────────────────────────────

    @Test
    fun `uiState reflects resolved officials`() = runTest {
        val officials = Officials(
            stateCode = "CA",
            stateName = "California",
            governor = "Gavin Newsom",
            senator1 = "Alex Padilla",
            senator2 = "Adam Schiff",
            representative = "Nancy Pelosi",
            stateCapital = "Sacramento"
        )
        every { repository.officialsFlow } returns flowOf(officials)

        val state = buildVm().uiState.first()
        assertTrue(state.officials.isResolved)
        assertEquals("CA", state.officials.stateCode)
        assertEquals("Gavin Newsom", state.officials.governor)
    }

    // ── mode flags ────────────────────────────────────────────────────────────

    @Test
    fun `uiState reflects is6520Mode from repository`() = runTest {
        every { repository.is6520Mode } returns flowOf(true)

        val state = buildVm().uiState.first()
        assertTrue(state.is6520Mode)
    }

    @Test
    fun `uiState reflects isOrderedMode from repository`() = runTest {
        every { repository.isOrderedMode } returns flowOf(true)

        val state = buildVm().uiState.first()
        assertTrue(state.isOrderedMode)
    }

    // ── progress stats ────────────────────────────────────────────────────────

    @Test
    fun `totalAttempted is zero when no progress`() = runTest {
        val state = buildVm().uiState.first()
        assertEquals(0, state.totalAttempted)
    }

    @Test
    fun `totalAttempted counts only questions with at least one attempt`() = runTest {
        val progress = mapOf(
            1 to QuestionProgress(questionId = 1, totalAttempts = 3, correctAttempts = 2),
            2 to QuestionProgress(questionId = 2, totalAttempts = 0, correctAttempts = 0), // not attempted
            3 to QuestionProgress(questionId = 3, totalAttempts = 1, correctAttempts = 1)
        )
        every { repository.allProgressFlow } returns flowOf(progress)

        val state = buildVm().uiState.first()
        assertEquals(2, state.totalAttempted)
    }

    @Test
    fun `overallAccuracy is zero when no questions attempted`() = runTest {
        val state = buildVm().uiState.first()
        assertEquals(0f, state.overallAccuracy)
    }

    @Test
    fun `overallAccuracy is average successRate of attempted questions`() = runTest {
        // Q1: 4/4 = 1.0, Q2: 1/2 = 0.5, Q3: 0/1 = 0.0 → average = 0.5
        val progress = mapOf(
            1 to QuestionProgress(questionId = 1, totalAttempts = 4, correctAttempts = 4),
            2 to QuestionProgress(questionId = 2, totalAttempts = 2, correctAttempts = 1),
            3 to QuestionProgress(questionId = 3, totalAttempts = 1, correctAttempts = 0)
        )
        every { repository.allProgressFlow } returns flowOf(progress)

        val state = buildVm().uiState.first()
        assertEquals(3, state.totalAttempted)
        // average of 1.0, 0.5, 0.0 = 0.5
        assertEquals(0.5f, state.overallAccuracy, 0.001f)
    }

    @Test
    fun `overallAccuracy excludes questions with zero attempts`() = runTest {
        val progress = mapOf(
            1 to QuestionProgress(questionId = 1, totalAttempts = 2, correctAttempts = 2), // 1.0
            2 to QuestionProgress(questionId = 2, totalAttempts = 0, correctAttempts = 0)  // excluded
        )
        every { repository.allProgressFlow } returns flowOf(progress)

        val state = buildVm().uiState.first()
        assertEquals(1, state.totalAttempted)
        assertEquals(1.0f, state.overallAccuracy, 0.001f)
    }

    // ── toggleOrderedMode ─────────────────────────────────────────────────────

    @Test
    fun `toggleOrderedMode calls repository setOrderedMode with true`() = runTest {
        val vm = buildVm()
        vm.toggleOrderedMode(true)
        advanceUntilIdle()
        coVerify { repository.setOrderedMode(true) }
    }

    @Test
    fun `toggleOrderedMode calls repository setOrderedMode with false`() = runTest {
        val vm = buildVm()
        vm.toggleOrderedMode(false)
        advanceUntilIdle()
        coVerify { repository.setOrderedMode(false) }
    }
}
