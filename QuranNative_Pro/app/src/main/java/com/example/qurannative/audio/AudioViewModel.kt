package com.example.qurannative.audio

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AudioUiState(
    val isPlaying: Boolean = false,
    val currentSurah: String = "",
    val currentReciter: String = "",
    val currentPosition: Long = 0L,
    val duration: Long = 0L,
    val volume: Float = 1f,
    val error: String? = null
)

@HiltViewModel
class AudioViewModel @Inject constructor(
    application: Application
) : AndroidViewModel(application) {

    private val player = ExoPlayer.Builder(application).build()

    private val _uiState = MutableStateFlow(AudioUiState())
    val uiState: StateFlow<AudioUiState> = _uiState.asStateFlow()

    private val listener = object : Player.Listener {

        override fun onIsPlayingChanged(isPlaying: Boolean) {
            updateState {
                copy(isPlaying = isPlaying)
            }
        }

        override fun onMediaItemTransition(
            mediaItem: MediaItem?,
            reason: Int
        ) {
            updateState {
                copy(
                    currentSurah = mediaItem?.mediaMetadata?.title
                        ?.toString() ?: ""
                )
            }
        }

        override fun onPlaybackStateChanged(playbackState: Int) {
            updateState {
                copy(
                    duration = player.duration
                        .takeIf { it > 0 } ?: 0L
                )
            }
        }

        override fun onPlayerError(error: androidx.media3.common.PlaybackException) {
            updateState {
                copy(
                    isPlaying = false,
                    error = error.message ?: "تعذر تشغيل الصوت"
                )
            }
        }
    }

    init {
        player.addListener(listener)
    }

    /**
     * تشغيل ملف صوتي.
     */
    fun playSurah(
        surahName: String,
        audioUrl: String,
        reciterName: String = ""
    ) {
        if (audioUrl.isBlank()) {
            updateState {
                copy(error = "رابط الصوت غير متوفر")
            }
            return
        }

        try {
            val mediaItem = MediaItem.Builder()
                .setUri(audioUrl)
                .setMediaMetadata(
                    androidx.media3.common.MediaMetadata.Builder()
                        .setTitle(surahName)
                        .setArtist(reciterName)
                        .build()
                )
                .build()

            player.setMediaItem(mediaItem)
            player.prepare()
            player.play()

            updateState {
                copy(
                    currentSurah = surahName,
                    currentReciter = reciterName,
                    currentPosition = 0L,
                    duration = 0L,
                    error = null
                )
            }

        } catch (exception: Exception) {
            updateState {
                copy(
                    error = exception.message ?: "حدث خطأ أثناء التشغيل"
                )
            }
        }
    }

    /**
     * إيقاف مؤقت.
     */
    fun pause() {
        player.pause()
    }

    /**
     * استكمال التشغيل.
     */
    fun resume() {
        player.play()
    }

    /**
     * إيقاف التشغيل.
     */
    fun stop() {
        player.stop()
        player.clearMediaItems()

        updateState {
            copy(
                isPlaying = false,
                currentPosition = 0L
            )
        }
    }

    /**
     * التحكم بمستوى الصوت.
     */
    fun setVolume(value: Float) {
        val safeVolume = value.coerceIn(0f, 1f)

        player.volume = safeVolume

        updateState {
            copy(volume = safeVolume)
        }
    }

    /**
     * تقديم أو إرجاع الصوت.
     */
    fun seekTo(position: Long) {
        player.seekTo(position.coerceAtLeast(0L))
        updatePosition()
    }

    /**
     * تحديث موضع التشغيل.
     */
    fun updatePosition() {
        updateState {
            copy(
                currentPosition = player.currentPosition.coerceAtLeast(0L),
                duration = player.duration.takeIf { it > 0 } ?: 0L
            )
        }
    }

    /**
     * تحديث حالة الواجهة.
     */
    private fun updateState(
        transform: AudioUiState.() -> AudioUiState
    ) {
        _uiState.value = _uiState.value.transform()
    }

    override fun onCleared() {
        player.removeListener(listener)
        player.release()

        super.onCleared()
    }
}
