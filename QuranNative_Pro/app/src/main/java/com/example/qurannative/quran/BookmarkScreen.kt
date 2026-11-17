package com.example.qurannative.quran

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun BookmarkScreen(
    viewModel: BookmarkViewModel,
    onOpenBookmark: (Int, Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val bookmarks by viewModel.bookmarks.collectAsState()
    var selectedId by remember { mutableStateOf<Int?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "العلامات المرجعية",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "الآيات التي حفظتها للعودة إليها",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
        )

        if (bookmarks.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("📖", style = MaterialTheme.typography.displayMedium)
                Text(
                    text = "لا توجد علامات محفوظة",
                    modifier = Modifier.padding(top = 12.dp),
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "أضف علامة أثناء القراءة لتظهر هنا",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(
                    items = bookmarks,
                    key = { it.id }
                ) { bookmark ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onOpenBookmark(
                                    bookmark.surahNumber,
                                    bookmark.ayahNumber
                                )
                            },
                        colors = CardDefaults.cardColors(
                            containerColor =
                                MaterialTheme.colorScheme.surfaceContainer
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = bookmark.surahName,
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.titleMedium
                                )

                                Text(
                                    text = "الآية ${bookmark.ayahNumber}",
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )

                                if (bookmark.note.isNotBlank()) {
                                    Text(
                                        text = bookmark.note,
                                        modifier = Modifier.padding(top = 5.dp),
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            }

                            TextButton(
                                onClick = {
                                    selectedId = bookmark.id
                                }
                            ) {
                                Text("حذف")
                            }
                        }
                    }
                }
            }
        }
    }

    if (selectedId != null) {
        AlertDialog(
            onDismissRequest = { selectedId = null },
            title = { Text("حذف العلامة؟") },
            text = { Text("هل تريد حذف هذه العلامة المرجعية؟") },
            confirmButton = {
                TextButton(
                    onClick = {
                        selectedId?.let(viewModel::deleteBookmark)
                        selectedId = null
                    }
                ) {
                    Text("حذف")
                }
            },
            dismissButton = {
                TextButton(onClick = { selectedId = null }) {
                    Text("رجوع")
                }
            }
        )
    }
}
