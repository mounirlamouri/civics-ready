package com.civicsready.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ProgressDao {

    @Query("SELECT * FROM progress")
    fun getAllProgress(): Flow<List<ProgressEntity>>

    @Query("SELECT * FROM progress WHERE questionId = :questionId")
    suspend fun getProgress(questionId: Int): ProgressEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entity: ProgressEntity)

    @Query(
        "INSERT INTO progress (questionId, totalAttempts, correctAttempts, lastAttemptTimestamp) " +
        "VALUES (:questionId, 1, CASE WHEN :correct THEN 1 ELSE 0 END, :timestamp) " +
        "ON CONFLICT(questionId) DO UPDATE SET " +
        "totalAttempts = totalAttempts + 1, " +
        "correctAttempts = correctAttempts + CASE WHEN :correct THEN 1 ELSE 0 END, " +
        "lastAttemptTimestamp = :timestamp"
    )
    suspend fun recordAnswer(questionId: Int, correct: Boolean, timestamp: Long)

    @Query("DELETE FROM progress")
    suspend fun clearAll()
}
