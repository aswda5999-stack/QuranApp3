package com.example.qurannative.audio

import android.content.ComponentName
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaController
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import androidx.media3.session.SessionToken
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.concurrent.Executor

// ======================================================
// نماذج الصوت
// ======================================================

data class QuranReciter(
    val id: String,
    val name: String,
    val country: String = "",
    val audioBaseUrl: String = ""
)

data class QuranAudioTrack(
    val surahNumber: Int,
    val surahName: String,
    val reciterName: String,
    val audioUrl: String
)

data class AudioUiState(
    val isConnected: Boolean = false,
    val isPlaying: Boolean = false,
    val isBuffering: Boolean = false,
    val position: Long = 0L,
    val duration: Long = 0L,
    val title: String = "",
    val artist: String = "",
    val error: String? = null
)

// ======================================================
// خدمة الوسائط: المشغل الوحيد داخل التطبيق
// ======================================================

class QuranMediaService : MediaSessionService() {

    private var player: ExoPlayer? = null
    private var mediaSession: MediaSession? = null

    override fun onCreate() {
        super.onCreate()

        val exoPlayer = ExoPlayer.Builder(this)
            .build()

        player = exoPlayer

        mediaSession = MediaSession.Builder(
            this,
            exoPlayer
        ).build()
    }

    override fun onGetSession(
        controllerInfo: MediaSession.ControllerInfo
    ): MediaSession? {
        return mediaSession
    }

    override fun onDestroy() {
        mediaSession?.run {
            player.release()
            release()
        }

        mediaSession = null
        player = null

        super.onDestroy()
    }
}

// ======================================================
// ViewModel: يتحكم بالخدمة ولا ينشئ مشغلاً آخر
// ======================================================

@HiltViewModel
class QuranAudioViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(AudioUiState())
    val uiState: StateFlow<AudioUiState> = _uiState.asStateFlow()

    private var controller: MediaController? = null

    private var controllerFuture:
        com.google.common.util.concurrent.ListenableFuture<MediaController>? = null

    private val executor = Executor { command ->
        command.run()
    }

    private val playerListener = object : Player.Listener {

        override fun onEvents(
            player: Player,
            events: Player.Events
        ) {
            updateState()
        }

        override fun onPlayerError(error: PlaybackException) {
            _uiState.value = _uiState.value.copy(
                error = error.message ?: "حدث خطأ أثناء تشغيل الصوت"
            )
        }
    }

    init {
        connectToService()

        viewModelScope.launch {
            while (true) {
                updateState()
                delay(500)
            }
        }
    }

    // --------------------------------------------------
    // الاتصال بخدمة الصوت
    // --------------------------------------------------

    private fun connectToService() {
        val token = SessionToken(
            context,
            ComponentName(
                context,
                QuranMediaService::class.java
            )
        )

        val future = MediaController.Builder(
            context,
            token
        ).buildAsync()

        controllerFuture = future

        future.addListener(
            {
                try {
                    val mediaController = future.get()

                    controller = mediaController
                    mediaController.addListener(playerListener)

                    _uiState.value = _uiState.value.copy(
                        isConnected = true,
                        error = null
                    )

                    updateState()

                } catch (exception: Exception) {
                    _uiState.value = _uiState.value.copy(
                        isConnected = false,
                        error = exception.message
                            ?: "تعذر الاتصال بمشغل الصوت"
                    )
                }
            },
            executor
        )
    }

    // --------------------------------------------------
    // تشغيل سورة واحدة
    // --------------------------------------------------

    fun playTrack(track: QuranAudioTrack) {
        val mediaController = controller ?: return

        if (track.audioUrl.isBlank()) {
            _uiState.value = _uiState.value.copy(
                error = "رابط التلاوة غير متوفر حالياً"
            )
            return
        }

        val mediaItem = createMediaItem(track)

        mediaController.setMediaItem(mediaItem)
        mediaController.prepare()
        mediaController.play()

        _uiState.value = _uiState.value.copy(
            error = null
        )
    }

    // --------------------------------------------------
    // تشغيل قائمة سور
    // --------------------------------------------------

    fun playQueue(
        tracks: List<QuranAudioTrack>,
        startIndex: Int = 0
    ) {
        val mediaController = controller ?: return

        if (tracks.isEmpty()) return

        val validTracks = tracks.filter {
            it.audioUrl.isNotBlank()
        }

        if (validTracks.isEmpty()) {
            _uiState.value = _uiState.value.copy(
                error = "لا توجد روابط صوتية صالحة"
            )
            return
        }

        val safeIndex = startIndex.coerceIn(
            0,
            validTracks.lastIndex
        )

        val items = validTracks.map(::createMediaItem)

        mediaController.setMediaItems(
            items,
            safeIndex,
            0L
        )

        mediaController.prepare()
        mediaController.play()

        _uiState.value = _uiState.value.copy(
            error = null
        )
    }

    private fun createMediaItem(
        track: QuranAudioTrack
    ): MediaItem {
        val metadata = MediaMetadata.Builder()
            .setTitle(track.surahName)
            .setArtist(track.reciterName)
            .setDisplayTitle(track.surahName)
            .build()

        return MediaItem.Builder()
            .setUri(track.audioUrl)
            .setMediaMetadata(metadata)
            .build()
    }

    // --------------------------------------------------
    // أزرار التحكم
    // --------------------------------------------------

    fun play() {
        controller?.play()
    }

    fun pause() {
        controller?.pause()
    }

    fun togglePlayback() {
        val mediaController = controller ?: return

        if (mediaController.isPlaying) {
            mediaController.pause()
        } else {
            mediaController.play()
        }
    }

    fun stop() {
        controller?.stop()
        updateState()
    }

    fun next() {
        controller?.seekToNextMediaItem()
    }

    fun previous() {
        controller?.seekToPreviousMediaItem()
    }

    fun seekTo(position: Long) {
        val mediaController = controller ?: return

        mediaController.seekTo(
            position.coerceAtLeast(0L)
        )
    }

    fun setVolume(volume: Float) {
        controller?.volume = volume.coerceIn(0f, 1f)
    }

    // --------------------------------------------------
    // تحديث حالة الواجهة
    // --------------------------------------------------

    private fun updateState() {
        val mediaController = controller ?: return

        val duration = mediaController.duration
            .takeIf { it >= 0L } ?: 0L

        val metadata = mediaController.mediaMetadata

        _uiState.value = AudioUiState(
            isConnected = true,
            isPlaying = mediaController.isPlaying,
            isBuffering = mediaController.playbackState ==
                Player.STATE_BUFFERING,
            position = mediaController.currentPosition
                .coerceAtLeast(0L),
            duration = duration,
            title = metadata.title?.toString().orEmpty(),
            artist = metadata.artist?.toString().orEmpty(),
            error = _uiState.value.error
        )
    }

    // --------------------------------------------------
    // تحرير الاتصال
    // --------------------------------------------------

    override fun onCleared() {
        controller?.removeListener(playerListener)
        controller = null

        controllerFuture?.let {
            MediaController.releaseFuture(it)
        }

        controllerFuture = null

        super.onCleared()
    }
}
