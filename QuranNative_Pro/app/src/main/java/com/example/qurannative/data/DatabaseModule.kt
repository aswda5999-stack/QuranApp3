package com.example.qurannative.data

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideQuranDatabase(
        @ApplicationContext context: Context
    ): QuranDatabase {
        return Room.databaseBuilder(
            context,
            QuranDatabase::class.java,
            "quran_database"
        ).build()
    }

    @Provides
    fun provideBookmarkDao(
        database: QuranDatabase
    ): BookmarkDao = database.bookmarkDao()

    @Provides
    fun provideTasbihDao(
        database: QuranDatabase
    ): TasbihDao = database.tasbihDao()

    @Provides
    fun provideReadingStateDao(
        database: QuranDatabase
    ): ReadingStateDao = database.readingStateDao()
}
