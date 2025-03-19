package com.example.mp3playerviacompose.data.device.mediaPlayer

import android.media.MediaPlayer
import android.util.Log
import com.example.mp3playerviacompose.domain.models.MediaModel
import com.example.mp3playerviacompose.domain.repositories.MediaPlayerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MediaPlayerManager @Inject constructor(): MediaPlayerRepository {

    private var mediaPlayer: MediaPlayer = MediaPlayer()
    private var currentMediaItem: MediaModel? = null
    val _currentPosition = MutableStateFlow(0)
    private var _isSongCompleted =  MutableStateFlow(false)
    private var isSongCompleted : StateFlow<Boolean> = _isSongCompleted.asStateFlow()


    override fun play(mediaItem: MediaModel){
        try {
            mediaPlayer.reset()
            mediaPlayer.setDataSource(mediaItem.path)
            mediaPlayer.prepareAsync()
            mediaPlayer.setOnPreparedListener {
                it.start()
                _isSongCompleted.value = false
                currentMediaItem = mediaItem
                _currentPosition.value = it.duration
            }
            mediaPlayer.setOnErrorListener { _, what, extra ->
                Log.e("MediaPlayerManager", "Error: what=$what, extra=$extra")
                true // Consume the error
            }
            mediaPlayer.setOnCompletionListener {
                _isSongCompleted.value = true
            }
        } catch (e: Exception) {
            Log.e("MediaPlayerManager", "Error playing media", e)
        }
    }

    override fun pause() {
        mediaPlayer.pause()
        // TODO: Notify UI about playback paused
    }

    override fun resume(){
        mediaPlayer.start()
    }

    override fun stop() {
        mediaPlayer.stop()
        mediaPlayer.reset()
        currentMediaItem = null
        // TODO: Notify UI about playback stopped
    }

    override fun isPlaying(): Boolean {
        return mediaPlayer.isPlaying ?: false
    }

    override fun release() {
        mediaPlayer.release()
    }

    override fun getCurrentMediaItem(): MediaModel? {
        return currentMediaItem
    }

    override fun getCurrentMusicDuration(): Int{
        return _currentPosition.value
    }

    override fun seekToPosition(position: Int){
        mediaPlayer.seekTo(position)
    }

    override fun isMusicCompleted(): Boolean{
        return isSongCompleted.value
    }

    override fun getMediaPlayer(): MediaPlayer{
        return mediaPlayer
    }
}