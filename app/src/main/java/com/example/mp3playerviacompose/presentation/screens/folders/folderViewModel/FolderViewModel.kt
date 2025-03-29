package com.example.mp3playerviacompose.presentation.screens.folders.folderViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mp3playerviacompose.domain.models.AudioFolder
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
class FolderViewModel @Inject constructor(private val mediaFetchingRepository: MediaFetchingRepository): ViewModel() {
    private val _audioFolderList = MutableStateFlow<RequestState<List<AudioFolder>>>(RequestState.Idle)
    val audioFolderList = _audioFolderList.asStateFlow()


    init {
        fetchAudioFolders()
    }

    private fun fetchAudioFolders(){
        viewModelScope.launch {
            mediaFetchingRepository.getAudioFolders().
                onStart {  }
                .onCompletion {  }
                .collect { audioFolderList->
                    _audioFolderList.emit(audioFolderList)
                }
        }
    }
}