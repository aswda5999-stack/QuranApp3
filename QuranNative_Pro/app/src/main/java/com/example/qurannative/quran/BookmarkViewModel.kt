package com.example.qurannative.quran

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.qurannative.data.BookmarkDao
import com.example.qurannative.data.BookmarkEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class BookmarkViewModel @Inject constructor(
    private val bookmarkDao: BookmarkDao
) : ViewModel() {

    val bookmarks = bookmarkDao.getAll().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList()
    )

    fun addBookmark(
        surahNumber: Int,
        ayahNumber: Int,
        surahName: String,
        note: String = ""
    ) {
        if (surahNumber !in 1..114 || ayahNumber < 1) return

        viewModelScope.launch {
            bookmarkDao.insert(
                BookmarkEntity(
                    surahNumber = surahNumber,
                    ayahNumber = ayahNumber,
                    surahName = surahName,
                    note = note,
                    createdAt = System.currentTimeMillis()
                )
            )
        }
    }

    fun deleteBookmark(id: Int) {
        viewModelScope.launch {
            bookmarkDao.delete(id)
        }
    }

    fun deleteAllBookmarks() {
        viewModelScope.launch {
            bookmarkDao.deleteAll()
        }
    }
}
