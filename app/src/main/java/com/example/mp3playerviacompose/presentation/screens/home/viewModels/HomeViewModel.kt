package com.example.mp3playerviacompose.presentation.screens.Home.viewModels


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mp3playerviacompose.domain.models.MediaModel
import com.example.mp3playerviacompose.domain.models.RequestState
import com.example.mp3playerviacompose.domain.repositories.MediaFetchingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val mediaFetchingRepository: MediaFetchingRepository): ViewModel() {
    private val _deviceAudiosList = MutableStateFlow<RequestState<List<MediaModel>>>(RequestState.Idle)
    val deviceAudioList: StateFlow<RequestState<List<MediaModel>>> = _deviceAudiosList.asStateFlow()

    var selectedMediaModel : MediaModel? = null
    var artistName: String? = null
    var albumName: String? = null
    var folderName: String? = null

    fun fetchAudioFiles(){
        _deviceAudiosList.value = RequestState.Loading
        viewModelScope.launch {
            mediaFetchingRepository.fetchAudioFiles()
                .onStart {
                    _deviceAudiosList.value = RequestState.Loading
                }
                .collect {audioList->
                    _deviceAudiosList.value = audioList
                }
        }
    }


    fun getLoadingList(): List<MediaModel>{
        return mediaFetchingRepository.getLoadingList()
    }

}