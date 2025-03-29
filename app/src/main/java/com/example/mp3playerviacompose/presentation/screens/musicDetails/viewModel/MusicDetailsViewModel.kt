package com.example.mp3playerviacompose.presentation.screens.musicDetails.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mp3playerviacompose.domain.models.MediaModel
import com.example.mp3playerviacompose.domain.models.RequestState
import com.example.mp3playerviacompose.domain.repositories.MediaFetchingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MusicDetailsViewModel @Inject constructor(private val mediaFetchingRepository: MediaFetchingRepository): ViewModel() {

    private val _musicByArtist = MutableStateFlow<RequestState<List<MediaModel>>>(RequestState.Idle)
    val musicByArtist: StateFlow<RequestState<List<MediaModel>>> = _musicByArtist

    private val _musicAlbums = MutableStateFlow<RequestState<List<MediaModel>>>(RequestState.Idle)
    val musicAlbums: StateFlow<RequestState<List<MediaModel>>> = _musicAlbums

    private val _folderTracks = MutableStateFlow<RequestState<List<MediaModel>>>(RequestState.Idle)
    val folderTracks: StateFlow<RequestState<List<MediaModel>>> = _folderTracks


    fun fetchSongsByArtist(artistName: String){
        viewModelScope.launch {
            mediaFetchingRepository.fetchAudioFilesByArtist(artistName).onStart {  }
                .onCompletion {  }
                .collect{ artistList ->
                    _musicByArtist.value = artistList
                }
        }
    }


    fun fetchAudioFilesByAlbums(albumName: String){
        viewModelScope.launch {
            mediaFetchingRepository.fetchAudioFilesByAlbums(albumName).onStart {  }
                .onCompletion {  }
                .collect{ albumList ->
                    _musicAlbums.value = albumList
                }
        }
    }


    fun fetchAudioFilesFromFolder(folderName: String){
        viewModelScope.launch {
            mediaFetchingRepository.fetchAudioFilesFromFolder(folderName).onStart {  }
                .onCompletion {  }
                .collect{ folderList ->
                    _folderTracks.value = folderList
                }
        }
    }

}