package com.civicsready.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "progress")
data class ProgressEntity(
    @PrimaryKey val questionId: Int,
    val totalAttempts: Int = 0,
    val correctAttempts: Int = 0,
    val lastAttemptTimestamp: Long = 0L
)
