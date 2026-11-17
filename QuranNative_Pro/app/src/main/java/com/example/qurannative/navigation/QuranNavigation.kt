package com.example.qurannative.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.qurannative.azkar.TasbihScreen
import com.example.qurannative.azkar.TasbihViewModel
import com.example.qurannative.quran.BookmarkScreen
import com.example.qurannative.quran.BookmarkViewModel
import com.example.qurannative.search.QuranSearchEntry
import com.example.qurannative.search.QuranSearchScreen

enum class QuranDestination(
    val title: String,
    val icon: String
) {
    QURAN("القرآن", "📖"),
    SEARCH("البحث", "🔎"),
    BOOKMARKS("المحفوظات", "🔖"),
    TASBIH("السبحة", "📿"),
    SETTINGS("الإعدادات", "⚙️")
}

@Composable
fun QuranAppShell(
    bookmarkViewModel: BookmarkViewModel,
    tasbihViewModel: TasbihViewModel,
    searchEntries: List<QuranSearchEntry>,
    onOpenAyah: (Int, Int) -> Unit,
    quranContent: @Composable () -> Unit,
    settingsContent: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    var destination by rememberSaveable {
        mutableStateOf(QuranDestination.QURAN)
    }

    Scaffold(
        modifier = modifier,
        bottomBar = {
            NavigationBar {
                QuranDestination.entries.forEach { item ->
                    NavigationBarItem(
                        selected = destination == item,
                        onClick = {
                            destination = item
                        },
                        icon = {
                            Text(item.icon)
                        },
                        label = {
                            Text(item.title)
                        }
                    )
                }
            }
        }
    ) { paddingValues ->

        when (destination) {
            QuranDestination.QURAN -> {
                androidx.compose.foundation.layout.Box(
                    modifier = Modifier.padding(paddingValues)
                ) {
                    quranContent()
                }
            }

            QuranDestination.SEARCH -> {
                QuranSearchScreen(
                    entries = searchEntries,
                    onOpenAyah = onOpenAyah,
                    modifier = Modifier.padding(paddingValues)
                )
            }

            QuranDestination.BOOKMARKS -> {
                BookmarkScreen(
                    viewModel = bookmarkViewModel,
                    onOpenBookmark = onOpenAyah,
                    modifier = Modifier.padding(paddingValues)
                )
            }

            QuranDestination.TASBIH -> {
                TasbihScreen(
                    viewModel = tasbihViewModel,
                    modifier = Modifier.padding(paddingValues)
                )
            }

            QuranDestination.SETTINGS -> {
                androidx.compose.foundation.layout.Box(
                    modifier = Modifier.padding(paddingValues)
                ) {
                    settingsContent()
                }
            }
        }
    }
}
