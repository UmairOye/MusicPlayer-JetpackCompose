package com.example.mp3playerviacompose.domain.repositories

import android.media.MediaPlayer
import com.example.mp3playerviacompose.domain.models.MediaModel

interface MediaPlayerRepository {
    fun play(mediaItem: MediaModel)
    fun pause()
    fun resume()
    fun stop()
    fun isPlaying(): Boolean
    fun release()
    fun getCurrentMediaItem(): MediaModel?
    fun seekToPosition(position: Int)
    fun isMusicCompleted(): Boolean
    fun getMediaPlayer(): MediaPlayer
    fun getCurrentMusicDuration(): Int
}