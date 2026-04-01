package com.civicsready.data.local

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ProgressDaoTest {

    private lateinit var database: CivicsDatabase
    private lateinit var dao: ProgressDao

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, CivicsDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        dao = database.progressDao()
    }

    @After
    fun closeDb() {
        database.close()
    }

    // ── getAllProgress ────────────────────────────────────────────────────────

    @Test
    fun getAllProgress_emitsEmptyListInitially() = runTest {
        val result = dao.getAllProgress().first()
        assertTrue(result.isEmpty())
    }

    @Test
    fun getAllProgress_emitsInsertedEntities() = runTest {
        val entity = ProgressEntity(questionId = 1, totalAttempts = 3, correctAttempts = 2)
        dao.upsert(entity)
        val result = dao.getAllProgress().first()
        assertEquals(1, result.size)
        assertEquals(entity, result.first())
    }

    @Test
    fun getAllProgress_emitsAllEntities() = runTest {
        dao.upsert(ProgressEntity(questionId = 1, totalAttempts = 2, correctAttempts = 1))
        dao.upsert(ProgressEntity(questionId = 5, totalAttempts = 4, correctAttempts = 4))
        dao.upsert(ProgressEntity(questionId = 128, totalAttempts = 1, correctAttempts = 0))
        val result = dao.getAllProgress().first()
        assertEquals(3, result.size)
    }

    // ── getProgress ───────────────────────────────────────────────────────────

    @Test
    fun getProgress_returnsNullForUnknownId() = runTest {
        val result = dao.getProgress(999)
        assertNull(result)
    }

    @Test
    fun getProgress_returnsCorrectEntityById() = runTest {
        dao.upsert(ProgressEntity(questionId = 10, totalAttempts = 5, correctAttempts = 3))
        val result = dao.getProgress(10)
        assertNotNull(result)
        assertEquals(10, result!!.questionId)
        assertEquals(5, result.totalAttempts)
        assertEquals(3, result.correctAttempts)
    }

    @Test
    fun getProgress_doesNotReturnWrongId() = runTest {
        dao.upsert(ProgressEntity(questionId = 10))
        assertNull(dao.getProgress(11))
    }

    // ── upsert ────────────────────────────────────────────────────────────────

    @Test
    fun upsert_insertsNewEntity() = runTest {
        val entity = ProgressEntity(questionId = 42, totalAttempts = 1, correctAttempts = 1)
        dao.upsert(entity)
        val result = dao.getProgress(42)
        assertNotNull(result)
        assertEquals(entity, result)
    }

    @Test
    fun upsert_replacesExistingEntityWithSameId() = runTest {
        dao.upsert(ProgressEntity(questionId = 7, totalAttempts = 2, correctAttempts = 1))
        dao.upsert(ProgressEntity(questionId = 7, totalAttempts = 5, correctAttempts = 4))
        val result = dao.getProgress(7)
        assertNotNull(result)
        assertEquals(5, result!!.totalAttempts)
        assertEquals(4, result.correctAttempts)
    }

    @Test
    fun upsert_preservesDefaultValues() = runTest {
        dao.upsert(ProgressEntity(questionId = 1))
        val result = dao.getProgress(1)
        assertNotNull(result)
        assertEquals(0, result!!.totalAttempts)
        assertEquals(0, result.correctAttempts)
        assertEquals(0L, result.lastAttemptTimestamp)
    }

    @Test
    fun upsert_storesTimestamp() = runTest {
        val timestamp = System.currentTimeMillis()
        dao.upsert(ProgressEntity(questionId = 1, lastAttemptTimestamp = timestamp))
        val result = dao.getProgress(1)
        assertEquals(timestamp, result!!.lastAttemptTimestamp)
    }

    // ── clearAll ──────────────────────────────────────────────────────────────

    @Test
    fun clearAll_removesAllEntities() = runTest {
        dao.upsert(ProgressEntity(questionId = 1))
        dao.upsert(ProgressEntity(questionId = 2))
        dao.upsert(ProgressEntity(questionId = 3))
        dao.clearAll()
        val result = dao.getAllProgress().first()
        assertTrue(result.isEmpty())
    }

    @Test
    fun clearAll_isIdempotentOnEmptyTable() = runTest {
        dao.clearAll() // no-op on empty table
        val result = dao.getAllProgress().first()
        assertTrue(result.isEmpty())
    }

    @Test
    fun clearAll_allowsInsertAfterClear() = runTest {
        dao.upsert(ProgressEntity(questionId = 1))
        dao.clearAll()
        dao.upsert(ProgressEntity(questionId = 1, totalAttempts = 1, correctAttempts = 1))
        val result = dao.getProgress(1)
        assertNotNull(result)
        assertEquals(1, result!!.totalAttempts)
    }

    // ── multiple question IDs ─────────────────────────────────────────────────

    @Test
    fun progressIsolation_separateEntriesForDifferentQuestions() = runTest {
        dao.upsert(ProgressEntity(questionId = 1, totalAttempts = 10, correctAttempts = 8))
        dao.upsert(ProgressEntity(questionId = 2, totalAttempts = 3, correctAttempts = 1))

        val q1 = dao.getProgress(1)
        val q2 = dao.getProgress(2)

        assertEquals(10, q1!!.totalAttempts)
        assertEquals(3, q2!!.totalAttempts)
    }

    @Test
    fun supports_all128QuestionIds() = runTest {
        // Verify the schema accommodates any valid question ID (1-128)
        for (id in listOf(1, 64, 128)) {
            dao.upsert(ProgressEntity(questionId = id, totalAttempts = 1, correctAttempts = 1))
        }
        val all = dao.getAllProgress().first()
        assertEquals(3, all.size)
        val ids = all.map { it.questionId }.toSet()
        assertTrue(1 in ids)
        assertTrue(64 in ids)
        assertTrue(128 in ids)
    }
}
