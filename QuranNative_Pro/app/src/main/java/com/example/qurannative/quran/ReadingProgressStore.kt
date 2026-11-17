package com.example.qurannative.quran

import android.content.Context
import androidx.core.content.edit

data class ReadingProgress(
    val surahNumber: Int,
    val ayahNumber: Int
)

class ReadingProgressStore(context: Context) {

    private val preferences = context.getSharedPreferences(
        "quran_reading_progress",
        Context.MODE_PRIVATE
    )

    fun save(surahNumber: Int, ayahNumber: Int) {
        if (surahNumber !in 1..114 || ayahNumber < 1) {
            return
        }

        preferences.edit {
            putInt(KEY_SURAH, surahNumber)
            putInt(KEY_AYAH, ayahNumber)
        }
    }

    fun get(): ReadingProgress {
        return ReadingProgress(
            surahNumber = preferences.getInt(KEY_SURAH, 1),
            ayahNumber = preferences.getInt(KEY_AYAH, 1)
        )
    }

    fun clear() {
        preferences.edit {
            remove(KEY_SURAH)
            remove(KEY_AYAH)
        }
    }

    companion object {
        private const val KEY_SURAH = "surah_number"
        private const val KEY_AYAH = "ayah_number"
    }
}
