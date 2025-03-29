package com.example.mp3playerviacompose.presentation.screens.player.playerViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mp3playerviacompose.domain.models.MediaModel
import com.example.mp3playerviacompose.domain.repositories.MediaPlayerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(private val mediaPlayerManager: MediaPlayerRepository): ViewModel() {
    private var isMusicPlaying: Boolean = false
    private val currentPlayerPosition_ = MutableStateFlow(0)
//    var currentIndex = -1

    val currentPlayerPosition: StateFlow<Int> = currentPlayerPosition_.asStateFlow()
    private fun updateCurrentPosition() {
        viewModelScope.launch {
            while (isMusicPlaying) {
                currentPlayerPosition_.emit(mediaPlayerManager.getMediaPlayer().currentPosition ?: 0)
                delay(1000)
            }
        }
    }

//    fun playNextSong(onSongChanged: (MediaModel) -> Unit) {
//        if (Utils.playerList.isNotEmpty()) {
//            currentIndex = if (currentIndex == Utils.playerList.size - 1) 0 else currentIndex + 1
//            val nextSong = Utils.playerList[currentIndex]
//            Utils.printLog(message = "nextIndex: $currentIndex")
//            playMusic(nextSong)
//            onSongChanged(nextSong)
//        }
//    }


    fun playMusic(mediaItem: MediaModel){
        mediaPlayerManager.play(mediaItem)
        isMusicPlaying = true
        updateCurrentPosition()
    }

    fun stopMusic(){
        mediaPlayerManager.stop()
        isMusicPlaying = false
    }

    fun pauseMusic(){
        mediaPlayerManager.pause()
        isMusicPlaying = false
    }

    fun resumeMusic(){
        mediaPlayerManager.resume()
        isMusicPlaying = true
    }

    fun releaseMusicPlayer(){
        mediaPlayerManager.release()
        isMusicPlaying = false
    }

    fun getCurrentPlayingItems(): MediaModel?{
        return mediaPlayerManager.getCurrentMediaItem()
    }


    fun isMusicPlaying(): Boolean{
        return mediaPlayerManager.isPlaying()
    }

    fun seekToPosition(position: Int){
        mediaPlayerManager.seekToPosition(position)
    }

    fun getCurrentMusicDuration(): Int{
        return mediaPlayerManager.getCurrentMusicDuration()
    }

    fun isSongCompleted(): Boolean{
        return mediaPlayerManager.isMusicCompleted()
    }
}