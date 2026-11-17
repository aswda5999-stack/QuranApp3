package com.example.qurannative.audio

import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService

class QuranMediaService : MediaSessionService() {

    private var mediaSession: MediaSession? = null
    private var exoPlayer: ExoPlayer? = null

    override fun onCreate() {
        super.onCreate()

        initializePlayer()
    }

    private fun initializePlayer() {
        val player = ExoPlayer.Builder(this)
            .build()

        player.setAudioAttributes(
            AudioAttributes.Builder()
                .setUsage(C.USAGE_MEDIA)
                .setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)
                .build(),
            true
        )

        player.repeatMode = Player.REPEAT_MODE_OFF

        exoPlayer = player

        mediaSession = MediaSession.Builder(
            this,
            player
        ).build()
    }

    override fun onGetSession(
        controllerInfo: MediaSession.ControllerInfo
    ): MediaSession? {
        return mediaSession
    }

    /**
     * تشغيل سورة أو ملف صوتي.
     */
    fun playAudio(url: String) {
        val player = exoPlayer ?: return

        if (url.isBlank()) return

        val mediaItem = MediaItem.fromUri(url)

        player.setMediaItem(mediaItem)
        player.prepare()
        player.play()
    }

    /**
     * إيقاف مؤقت.
     */
    fun pauseAudio() {
        exoPlayer?.pause()
    }

    /**
     * استكمال التشغيل.
     */
    fun resumeAudio() {
        exoPlayer?.play()
    }

    /**
     * إيقاف الصوت نهائياً.
     */
    fun stopAudio() {
        exoPlayer?.stop()
        exoPlayer?.clearMediaItems()
    }

    /**
     * الانتقال إلى وقت محدد.
     */
    fun seekTo(position: Long) {
        exoPlayer?.seekTo(position)
    }

    override fun onDestroy() {
        mediaSession?.run {
            player.release()
            release()
        }

        mediaSession = null
        exoPlayer = null

        super.onDestroy()
    }
}
