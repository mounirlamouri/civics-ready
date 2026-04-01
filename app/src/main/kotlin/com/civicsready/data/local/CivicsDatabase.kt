package com.civicsready.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ProgressEntity::class], version = 1, exportSchema = false)
abstract class CivicsDatabase : RoomDatabase() {
    abstract fun progressDao(): ProgressDao
}
