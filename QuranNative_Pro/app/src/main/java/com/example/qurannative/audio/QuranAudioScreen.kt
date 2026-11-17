package com.example.qurannative.audio

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun QuranAudioScreen(
    viewModel: QuranAudioViewModel,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "الاستماع للقرآن الكريم",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "استمع إلى تلاوتك المفضلة",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 5.dp)
        )

        Spacer(Modifier.height(30.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainer
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(22.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "📖",
                    style = MaterialTheme.typography.displayLarge
                )

                Text(
                    text = state.title.ifBlank {
                        "لم يتم اختيار سورة"
                    },
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 16.dp),
                    textAlign = TextAlign.Center
                )

                Text(
                    text = state.artist.ifBlank {
                        "اختر قارئاً لبدء الاستماع"
                    },
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 6.dp)
                )

                Spacer(Modifier.height(22.dp))

                Slider(
                    value = state.position.toFloat().coerceIn(
                        0f,
                        state.duration.coerceAtLeast(1L).toFloat()
                    ),
                    onValueChange = {
                        viewModel.seekTo(it.toLong())
                    },
                    valueRange = 0f..state.duration
                        .coerceAtLeast(1L).toFloat(),
                    enabled = state.duration > 0L,
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(formatAudioTime(state.position))
                    Text(formatAudioTime(state.duration))
                }

                Spacer(Modifier.height(20.dp))

                if (state.isBuffering) {
                    CircularProgressIndicator()
                    Spacer(Modifier.height(12.dp))
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(
                        onClick = viewModel::previous,
                        enabled = state.isConnected
                    ) {
                        Text("السابق")
                    }

                    Button(
                        onClick = viewModel::togglePlayback,
                        enabled = state.isConnected
                    ) {
                        Text(
                            if (state.isPlaying) "إيقاف مؤقت"
                            else "تشغيل"
                        )
                    }

                    OutlinedButton(
                        onClick = viewModel::next,
                        enabled = state.isConnected
                    ) {
                        Text("التالي")
                    }
                }

                Spacer(Modifier.height(10.dp))

                OutlinedButton(
                    onClick = viewModel::stop,
                    enabled = state.isConnected,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("إيقاف التلاوة")
                }

                if (!state.isConnected) {
                    Text(
                        text = "جارٍ الاتصال بمشغل الصوت...",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 12.dp)
                    )
                }

                state.error?.let { error ->
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(top = 12.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

private fun formatAudioTime(milliseconds: Long): String {
    val totalSeconds = milliseconds.coerceAtLeast(0L) / 1000L
    val hours = totalSeconds / 3600L
    val minutes = (totalSeconds % 3600L) / 60L
    val seconds = totalSeconds % 60L

    return if (hours > 0L) {
        "%d:%02d:%02d".format(hours, minutes, seconds)
    } else {
        "%02d:%02d".format(minutes, seconds)
    }
}
