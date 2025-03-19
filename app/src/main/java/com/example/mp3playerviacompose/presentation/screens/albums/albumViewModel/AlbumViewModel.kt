package com.example.mp3playerviacompose.presentation.screens.albums.albumViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mp3playerviacompose.domain.models.Album
import com.example.mp3playerviacompose.domain.models.RequestState
import com.example.mp3playerviacompose.domain.repositories.MediaFetchingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlbumViewModel @Inject constructor(private val mediaFetchingRepository: MediaFetchingRepository): ViewModel() {
    private val _albumList = MutableStateFlow<RequestState<List<Album>>>(RequestState.Idle)
    val albumList = _albumList.asStateFlow()

    fun fetchAlbums(){
       viewModelScope.launch {
           mediaFetchingRepository.getAlbums().onStart {  }
               .onCompletion {  }
               .collect { albumList->
                   _albumList.emit(albumList)
               }
       }
    }

}