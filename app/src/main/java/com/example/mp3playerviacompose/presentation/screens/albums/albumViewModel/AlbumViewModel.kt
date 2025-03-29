package com.example.mp3playerviacompose.presentation.screens.albums.albumViewModel

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mp3playerviacompose.domain.models.Album
import com.example.mp3playerviacompose.domain.models.RequestState
import com.example.mp3playerviacompose.domain.repositories.MediaFetchingRepository
import com.example.mp3playerviacompose.domain.utils.Constants
import com.example.mp3playerviacompose.domain.utils.hasPermission
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@HiltViewModel
class AlbumViewModel @Inject constructor(private val mediaFetchingRepository: MediaFetchingRepository,
    @ApplicationContext context: Context): ViewModel() {
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