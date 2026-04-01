package com.civicsready.di

import android.content.Context
import androidx.room.Room
import com.civicsready.data.local.CivicsDatabase
import com.civicsready.data.local.ProgressDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): CivicsDatabase =
        Room.databaseBuilder(context, CivicsDatabase::class.java, "civics_db")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideProgressDao(db: CivicsDatabase): ProgressDao = db.progressDao()
}
