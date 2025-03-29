package com.example.mp3playerviacompose.presentation.screens.playList.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mp3playerviacompose.domain.models.MediaModel
import com.example.mp3playerviacompose.domain.models.PlayListEntity
import com.example.mp3playerviacompose.domain.models.RequestState
import com.example.mp3playerviacompose.domain.models.SongsPlayList
import com.example.mp3playerviacompose.domain.repositories.PlayListRepository
import com.example.mp3playerviacompose.domain.utils.Utils
import com.example.mp3playerviacompose.domain.utils.Utils.playerList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlayListViewModel @Inject constructor(private val playListRepository: PlayListRepository) :
    ViewModel() {
    val selectedPlayList = mutableListOf<PlayListEntity>()
    private val _allPlaylist = MutableStateFlow<RequestState<List<PlayListEntity>>>(RequestState.Idle)
    val allPlaylist = _allPlaylist.asStateFlow()

    private val _allPlayListSongs = MutableStateFlow<RequestState<List<SongsPlayList>>>(RequestState.Idle)
    val allPlaylistSongs = _allPlayListSongs.asStateFlow()

    init {
        getAllPlayLists()
        getAllPlaylistSongs()
    }

    private fun getAllPlaylistSongs(){
        viewModelScope.launch {
            playListRepository.getSongsFromPlayList()
                .onStart {  }
                .onCompletion {  }
                .collect{ songPlayList->
                    _allPlayListSongs.value = RequestState.Success(songPlayList)
                }
        }
    }

    fun createPlayList(playListName: String) {
        viewModelScope.launch {
            val playListEntity = PlayListEntity(
                id = 0,
                playListName = playListName,
                songDuration = 0,
                songsCount = 0
            )
            playListRepository.createPlayList(playListEntity)
        }
    }


    private fun insertPlayList(songsPlayList: SongsPlayList) {
        viewModelScope.launch {
            playListRepository.insertSongsIntoPlayList(songsPlayList)
        }
    }


    fun insertSongIntoPlayList(mediaModel: MediaModel?) {
        viewModelScope.launch {
            if (selectedPlayList.isNotEmpty()) {
                selectedPlayList.forEach { playListEntity ->
                    mediaModel?.let {
                        val songsPlayList = SongsPlayList(
                            name = it.name,
                            artist = it.artist,
                            uri = it.uri,
                            path = it.path,
                            duration = it.duration,
                            formattedDuration = it.formattedDuration,
                            size = it.size,
                            formattedSize = it.formattedSize,
                            thumbnail = it.thumbnail,
                            isFavorite = it.isFavorite,
                            playListName = playListEntity.playListName
                        )
                        insertPlayList(songsPlayList)
                    }
                }
            }
        }

    }


    private fun getAllPlayLists() {
        viewModelScope.launch {
            _allPlaylist.value = RequestState.Loading
            playListRepository.getPlayList()
                .onStart { }
                .onCompletion { }
                .collect { allPlaylists ->
                    _allPlaylist.value = RequestState.Success(allPlaylists)
                }
        }
    }

    fun deletePlayList(playListEntity: PlayListEntity) {
        viewModelScope.launch {
            playListRepository.deletePlayList(playListEntity)
        }
    }


    fun makePlayList(playListEntity: PlayListEntity) {
        selectedPlayList.add(playListEntity)
    }

    fun removeFromPlayList(playListEntity: PlayListEntity) {
        selectedPlayList.remove(playListEntity)
    }


    fun deleteFromPlayList(playList: SongsPlayList){
        viewModelScope.launch {
            playListRepository.deleteFromPlaylist(playList)
        }
    }


    fun addSongsToPlaylist(songs: List<SongsPlayList>) {
       viewModelScope.launch(Dispatchers.IO) {
           val mediaList = songs.map { song ->
               MediaModel(
                   mediaId = song.id.toInt(),
                   name = song.name,
                   artist = song.artist,
                   uri = song.uri,
                   path = song.path,
                   duration = song.duration,
                   formattedDuration = song.formattedDuration,
                   size = song.size,
                   formattedSize = song.formattedSize,
                   thumbnail = song.thumbnail,
                   isFavorite = song.isFavorite,
                   playListName = song.playListName
               )
           }
           playerList.addAll(mediaList)
       }
    }



    fun updatePlayListFavorites(path: String, isFavorite: Boolean){
        viewModelScope.launch {
            playListRepository.updatePlayListFavorite(path, isFavorite)
        }
    }
}