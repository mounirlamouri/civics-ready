@file:OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)

package com.civicsready.ui.settings

import com.civicsready.data.assets.ZipLookupResult
import com.civicsready.data.repository.CivicsRepository
import com.civicsready.domain.model.Officials
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

class SettingsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository: CivicsRepository = mockk()

    @Before
    fun setup() {
        every { repository.officialsFlow } returns flowOf(Officials())
        every { repository.is6520Mode } returns flowOf(false)
        coEvery { repository.set6520Mode(any()) } just Runs
        coEvery { repository.setOrderedMode(any()) } just Runs
    }

    private fun buildVm() = SettingsViewModel(repository)

    // ── onZipChanged ──────────────────────────────────────────────────────────

    @Test
    fun `onZipChanged accepts digit input up to 5 characters`() = runTest {
        val vm = buildVm()
        advanceUntilIdle()
        vm.onZipChanged("94102")
        assertEquals("94102", vm.uiState.value.zipInput)
    }

    @Test
    fun `onZipChanged ignores non-digit characters`() = runTest {
        val vm = buildVm()
        advanceUntilIdle()
        vm.onZipChanged("abc")
        assertEquals("", vm.uiState.value.zipInput)
    }

    @Test
    fun `onZipChanged ignores input longer than 5 characters`() = runTest {
        val vm = buildVm()
        advanceUntilIdle()
        vm.onZipChanged("941020")
        assertEquals("", vm.uiState.value.zipInput)
    }

    @Test
    fun `onZipChanged rejects mixed digit and letter input`() = runTest {
        val vm = buildVm()
        advanceUntilIdle()
        vm.onZipChanged("9410a")
        assertEquals("", vm.uiState.value.zipInput)
    }

    @Test
    fun `onZipChanged clears lookupError`() = runTest {
        val vm = buildVm()
        advanceUntilIdle()
        // First force an error
        vm.lookupZip()
        advanceUntilIdle()
        assertNotNull(vm.uiState.value.lookupError)
        // Then typing should clear it
        vm.onZipChanged("94102")
        assertNull(vm.uiState.value.lookupError)
    }

    @Test
    fun `onZipChanged accepts partial zip codes`() = runTest {
        val vm = buildVm()
        advanceUntilIdle()
        vm.onZipChanged("941")
        assertEquals("941", vm.uiState.value.zipInput)
    }

    // ── lookupZip ─────────────────────────────────────────────────────────────

    @Test
    fun `lookupZip sets error when zipInput is less than 5 digits`() = runTest {
        val vm = buildVm()
        advanceUntilIdle()
        vm.onZipChanged("941")
        vm.lookupZip()
        advanceUntilIdle()
        assertNotNull(vm.uiState.value.lookupError)
        assertEquals("Please enter a 5-digit zip code.", vm.uiState.value.lookupError)
    }

    @Test
    fun `lookupZip sets error when zipInput is empty`() = runTest {
        val vm = buildVm()
        advanceUntilIdle()
        vm.lookupZip()
        advanceUntilIdle()
        assertNotNull(vm.uiState.value.lookupError)
    }

    @Test
    fun `lookupZip sets error when zip not found`() = runTest {
        coEvery { repository.resolveZipCode("00000") } returns null

        val vm = buildVm()
        advanceUntilIdle()
        vm.onZipChanged("00000")
        vm.lookupZip()
        advanceUntilIdle()

        assertNotNull(vm.uiState.value.lookupError)
        assertEquals(
            "Zip code not found. Please verify your zip and try again.",
            vm.uiState.value.lookupError
        )
        assertFalse(vm.uiState.value.isLoading)
    }

    @Test
    fun `lookupZip clears error on success`() = runTest {
        val result = ZipLookupResult(
            stateCode = "CA",
            stateName = "California",
            governor = "Gavin Newsom",
            senator1 = "Alex Padilla",
            senator2 = "Adam Schiff",
            representative = "Nancy Pelosi",
            stateCapital = "Sacramento",
            isExactMatch = true
        )
        coEvery { repository.resolveZipCode("94102") } returns result

        val vm = buildVm()
        advanceUntilIdle()
        vm.onZipChanged("94102")
        vm.lookupZip()
        advanceUntilIdle()

        assertNull(vm.uiState.value.lookupError)
        assertNull(vm.uiState.value.lookupNotice)
        assertFalse(vm.uiState.value.isLoading)
    }

    @Test
    fun `lookupZip sets notice when partial match`() = runTest {
        val partialResult = ZipLookupResult(
            stateCode = "CA",
            stateName = "California",
            governor = "Gavin Newsom",
            senator1 = "Alex Padilla",
            senator2 = "Adam Schiff",
            representative = "Visit house.gov/zip4",
            stateCapital = "Sacramento",
            isExactMatch = false
        )
        coEvery { repository.resolveZipCode("94999") } returns partialResult

        val vm = buildVm()
        advanceUntilIdle()
        vm.onZipChanged("94999")
        vm.lookupZip()
        advanceUntilIdle()

        assertNotNull(vm.uiState.value.lookupNotice)
        assertNull(vm.uiState.value.lookupError)
        assertFalse(vm.uiState.value.isLoading)
    }

    // ── toggle6520Mode ────────────────────────────────────────────────────────

    @Test
    fun `toggle6520Mode calls repository set6520Mode with true`() = runTest {
        val vm = buildVm()
        advanceUntilIdle()
        vm.toggle6520Mode(true)
        advanceUntilIdle()
        coVerify { repository.set6520Mode(true) }
    }

    @Test
    fun `toggle6520Mode calls repository set6520Mode with false`() = runTest {
        val vm = buildVm()
        advanceUntilIdle()
        vm.toggle6520Mode(false)
        advanceUntilIdle()
        coVerify { repository.set6520Mode(false) }
    }

    // ── init: loads from repository ───────────────────────────────────────────

    @Test
    fun `init loads officials from repository`() = runTest {
        val officials = Officials(
            stateCode = "TX",
            stateName = "Texas",
            governor = "Greg Abbott",
            senator1 = "John Cornyn",
            senator2 = "Ted Cruz",
            representative = "Some Rep",
            stateCapital = "Austin",
            zipCode = "78701"
        )
        every { repository.officialsFlow } returns flowOf(officials)

        val vm = buildVm()
        advanceUntilIdle()

        assertEquals("78701", vm.uiState.value.zipInput)
        assertTrue(vm.uiState.value.officials.isResolved)
        assertEquals("TX", vm.uiState.value.officials.stateCode)
    }

    @Test
    fun `init loads 6520 mode from repository`() = runTest {
        every { repository.is6520Mode } returns flowOf(true)

        val vm = buildVm()
        advanceUntilIdle()
        assertTrue(vm.uiState.value.is6520Mode)
    }
}
