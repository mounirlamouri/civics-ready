package com.civicsready.data.repository

import com.civicsready.data.assets.OfficialAssetLoader
import com.civicsready.data.assets.ZipLookupResult
import com.civicsready.data.local.ProgressDao
import com.civicsready.data.local.ProgressEntity
import com.civicsready.data.preferences.UserPreferences
import com.civicsready.domain.model.ALL_QUESTIONS
import com.civicsready.domain.model.QUESTIONS_6520
import com.civicsready.domain.model.DynamicAnswerType
import com.civicsready.domain.model.FederalOfficials
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.Runs
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class CivicsRepositoryTest {

    private val progressDao: ProgressDao = mockk()
    private val userPrefs: UserPreferences = mockk()
    private val assetLoader: OfficialAssetLoader = mockk()

    @Before
    fun setupDefaultMocks() {
        every { userPrefs.stateCode } returns flowOf("")
        every { userPrefs.stateName } returns flowOf("")
        every { userPrefs.governor } returns flowOf("")
        every { userPrefs.senator1 } returns flowOf("")
        every { userPrefs.senator2 } returns flowOf("")
        every { userPrefs.representative } returns flowOf("")
        every { userPrefs.stateCapital } returns flowOf("")
        every { userPrefs.zipCode } returns flowOf("")
        every { userPrefs.is6520Mode } returns flowOf(false)
        every { userPrefs.isOrderedMode } returns flowOf(false)
        every { progressDao.getAllProgress() } returns flowOf(emptyList())
    }

    private fun buildRepo() = CivicsRepository(progressDao, userPrefs, assetLoader)

    // ── officialsFlow ─────────────────────────────────────────────────────────

    @Test
    fun `officialsFlow is unresolved when preferences are empty`() = runTest {
        val officials = buildRepo().officialsFlow.first()
        assertFalse(officials.isResolved)
    }

    @Test
    fun `officialsFlow is resolved when stateCode is set`() = runTest {
        every { userPrefs.stateCode } returns flowOf("TX")
        every { userPrefs.stateName } returns flowOf("Texas")
        every { userPrefs.governor } returns flowOf("Greg Abbott")
        every { userPrefs.senator1 } returns flowOf("John Cornyn")
        every { userPrefs.senator2 } returns flowOf("Ted Cruz")
        every { userPrefs.representative } returns flowOf("Some Rep")
        every { userPrefs.stateCapital } returns flowOf("Austin")
        every { userPrefs.zipCode } returns flowOf("78701")

        val officials = buildRepo().officialsFlow.first()
        assertTrue(officials.isResolved)
        assertEquals("TX", officials.stateCode)
        assertEquals("Texas", officials.stateName)
        assertEquals("Greg Abbott", officials.governor)
        assertEquals("John Cornyn", officials.senator1)
        assertEquals("Ted Cruz", officials.senator2)
        assertEquals("Austin", officials.stateCapital)
    }

    // ── getQuestions: counts ──────────────────────────────────────────────────

    @Test
    fun `getQuestions returns all 128 questions in standard mode`() = runTest {
        val questions = buildRepo().getQuestions(for6520Only = false)
        assertEquals(128, questions.size)
    }

    @Test
    fun `getQuestions returns 20 questions in 6520 mode`() = runTest {
        val questions = buildRepo().getQuestions(for6520Only = true)
        assertEquals(20, questions.size)
    }

    @Test
    fun `getQuestions in 6520 mode only returns isFor6520 questions`() = runTest {
        val questions = buildRepo().getQuestions(for6520Only = true)
        assertTrue(questions.all { it.isFor6520 })
    }

    // ── getQuestions: federal dynamic answers (always resolved) ───────────────

    @Test
    fun `getQuestions resolves PRESIDENT for Q38`() = runTest {
        val q38 = buildRepo().getQuestions().first { it.id == 38 }
        assertEquals(listOf(FederalOfficials.PRESIDENT), q38.acceptableAnswers)
        assertEquals(DynamicAnswerType.PRESIDENT, q38.dynamicAnswerType)
    }

    @Test
    fun `getQuestions resolves VICE_PRESIDENT for Q39`() = runTest {
        val q39 = buildRepo().getQuestions().first { it.id == 39 }
        assertEquals(listOf(FederalOfficials.VICE_PRESIDENT), q39.acceptableAnswers)
    }

    @Test
    fun `getQuestions resolves SPEAKER_OF_HOUSE for Q30`() = runTest {
        val q30 = buildRepo().getQuestions().first { it.id == 30 }
        assertEquals(listOf(FederalOfficials.SPEAKER_OF_HOUSE), q30.acceptableAnswers)
    }

    @Test
    fun `getQuestions resolves CHIEF_JUSTICE for Q57`() = runTest {
        val q57 = buildRepo().getQuestions().first { it.id == 57 }
        assertEquals(listOf(FederalOfficials.CHIEF_JUSTICE), q57.acceptableAnswers)
    }

    // ── getQuestions: location-based dynamic answers ──────────────────────────

    @Test
    fun `getQuestions resolves GOVERNOR for Q61 from officials`() = runTest {
        every { userPrefs.stateCode } returns flowOf("CA")
        every { userPrefs.governor } returns flowOf("Gavin Newsom")

        val q61 = buildRepo().getQuestions().first { it.id == 61 }
        assertEquals(listOf("Gavin Newsom"), q61.acceptableAnswers)
    }

    @Test
    fun `getQuestions resolves both SENATORS for Q23`() = runTest {
        every { userPrefs.senator1 } returns flowOf("Alex Padilla")
        every { userPrefs.senator2 } returns flowOf("Adam Schiff")

        val q23 = buildRepo().getQuestions().first { it.id == 23 }
        assertEquals(listOf("Alex Padilla", "Adam Schiff"), q23.acceptableAnswers)
    }

    @Test
    fun `getQuestions includes only non-blank senators for Q23`() = runTest {
        every { userPrefs.senator1 } returns flowOf("Alex Padilla")
        every { userPrefs.senator2 } returns flowOf("")   // blank senator2

        val q23 = buildRepo().getQuestions().first { it.id == 23 }
        assertEquals(listOf("Alex Padilla"), q23.acceptableAnswers)
    }

    @Test
    fun `getQuestions resolves REPRESENTATIVE for Q29`() = runTest {
        every { userPrefs.representative } returns flowOf("Nancy Pelosi")

        val q29 = buildRepo().getQuestions().first { it.id == 29 }
        assertEquals(listOf("Nancy Pelosi"), q29.acceptableAnswers)
    }

    @Test
    fun `getQuestions resolves STATE_CAPITAL for Q62`() = runTest {
        every { userPrefs.stateCapital } returns flowOf("Sacramento")

        val q62 = buildRepo().getQuestions().first { it.id == 62 }
        assertEquals(listOf("Sacramento"), q62.acceptableAnswers)
    }

    @Test
    fun `getQuestions keeps original answers when governor is blank`() = runTest {
        // governor defaults to "" — filterNotBlank produces empty list → original kept
        val q61 = buildRepo().getQuestions().first { it.id == 61 }
        assertEquals(ALL_QUESTIONS.first { it.id == 61 }.acceptableAnswers, q61.acceptableAnswers)
    }

    @Test
    fun `getQuestions does not alter non-dynamic questions`() = runTest {
        val staticQuestion = ALL_QUESTIONS.first { it.dynamicAnswerType == null }
        val fromRepo = buildRepo().getQuestions().first { it.id == staticQuestion.id }
        assertEquals(staticQuestion.acceptableAnswers, fromRepo.acceptableAnswers)
    }

    // ── recordAnswer (atomic DAO) ──────────────────────────────────────────────

    @Test
    fun `recordAnswer delegates to atomic DAO method with correct=true`() = runTest {
        coEvery { progressDao.recordAnswer(any(), any(), any()) } just Runs

        buildRepo().recordAnswer(questionId = 1, correct = true)

        coVerify {
            progressDao.recordAnswer(1, true, any())
        }
    }

    @Test
    fun `recordAnswer delegates to atomic DAO method with correct=false`() = runTest {
        coEvery { progressDao.recordAnswer(any(), any(), any()) } just Runs

        buildRepo().recordAnswer(questionId = 5, correct = false)

        coVerify {
            progressDao.recordAnswer(5, false, any())
        }
    }

    @Test
    fun `recordAnswer passes a recent timestamp`() = runTest {
        coEvery { progressDao.recordAnswer(any(), any(), any()) } just Runs

        val before = System.currentTimeMillis()
        buildRepo().recordAnswer(questionId = 10, correct = true)
        val after = System.currentTimeMillis()

        coVerify {
            progressDao.recordAnswer(10, true, match { it in before..after })
        }
    }

    // ── allProgressFlow ───────────────────────────────────────────────────────

    @Test
    fun `allProgressFlow emits empty map when no progress`() = runTest {
        val result = buildRepo().allProgressFlow.first()
        assertTrue(result.isEmpty())
    }

    @Test
    fun `allProgressFlow maps entities to QuestionProgress keyed by questionId`() = runTest {
        val entities = listOf(
            ProgressEntity(questionId = 1, totalAttempts = 5, correctAttempts = 4),
            ProgressEntity(questionId = 2, totalAttempts = 2, correctAttempts = 1)
        )
        every { progressDao.getAllProgress() } returns flowOf(entities)

        val result = buildRepo().allProgressFlow.first()
        assertEquals(2, result.size)
        assertEquals(5, result[1]?.totalAttempts)
        assertEquals(4, result[1]?.correctAttempts)
        assertEquals(0.5f, result[2]?.successRate)
    }

    // ── resolveZipCode ────────────────────────────────────────────────────────

    @Test
    fun `resolveZipCode returns null when assetLoader returns null`() = runTest {
        every { assetLoader.lookup("00000") } returns null

        val result = buildRepo().resolveZipCode("00000")
        assertNull(result)
    }

    @Test
    fun `resolveZipCode persists and returns result on success`() = runTest {
        val lookupResult = ZipLookupResult(
            stateCode = "CA",
            stateName = "California",
            governor = "Gavin Newsom",
            senator1 = "Alex Padilla",
            senator2 = "Adam Schiff",
            representative = "Nancy Pelosi",
            stateCapital = "Sacramento",
            isExactMatch = true
        )
        every { assetLoader.lookup("94102") } returns lookupResult
        coEvery { userPrefs.saveOfficials(any(), any(), any(), any(), any(), any(), any(), any()) } just Runs

        val result = buildRepo().resolveZipCode("94102")
        assertNotNull(result)
        assertEquals("CA", result!!.stateCode)
        assertEquals(true, result.isExactMatch)

        coVerify {
            userPrefs.saveOfficials(
                zipCode = "94102",
                stateCode = "CA",
                stateName = "California",
                governor = "Gavin Newsom",
                senator1 = "Alex Padilla",
                senator2 = "Adam Schiff",
                representative = "Nancy Pelosi",
                stateCapital = "Sacramento"
            )
        }
    }

    @Test
    fun `resolveZipCode returns partial result with isExactMatch false`() = runTest {
        val lookupResult = ZipLookupResult(
            stateCode = "CA",
            stateName = "California",
            governor = "Gavin Newsom",
            senator1 = "Alex Padilla",
            senator2 = "Adam Schiff",
            representative = "Visit house.gov/zip4 to look up your Representative",
            stateCapital = "Sacramento",
            isExactMatch = false
        )
        every { assetLoader.lookup("94999") } returns lookupResult
        coEvery { userPrefs.saveOfficials(any(), any(), any(), any(), any(), any(), any(), any()) } just Runs

        val result = buildRepo().resolveZipCode("94999")
        assertNotNull(result)
        assertFalse(result!!.isExactMatch)
    }
}
