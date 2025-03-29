package com.example.mp3playerviacompose.presentation.screens.Home.viewModels


import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mp3playerviacompose.domain.models.MediaModel
import com.example.mp3playerviacompose.domain.models.PlayListEntity
import com.example.mp3playerviacompose.domain.models.RequestState
import com.example.mp3playerviacompose.domain.repositories.FavoriteRepository
import com.example.mp3playerviacompose.domain.repositories.MediaFetchingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val mediaFetchingRepository: MediaFetchingRepository,
    private val favoriteRepository: FavoriteRepository
): ViewModel() {
    private val _deviceAudiosList = MutableStateFlow<RequestState<SnapshotStateList<MediaModel>>>(RequestState.Idle)
    val deviceAudioList: StateFlow<RequestState<List<MediaModel>>> = _deviceAudiosList.asStateFlow()

    var wantToAddPlayList: Boolean = false

    private val _favorites = MutableStateFlow<RequestState<List<MediaModel>>>(RequestState.Idle)
    val favorites: StateFlow<RequestState<List<MediaModel>>> = _favorites.asStateFlow()


    var selectedMediaModel : MediaModel? = null
    var artistName: String? = null
    var albumName: String? = null
    var folderName: String? = null

    init {
        fetchAudioFiles()
        getAllFavorites()
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
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


    fun insertFavorite(mediaModel: MediaModel, isFavorite: Boolean = true){
        viewModelScope.launch {
            val index = (_deviceAudiosList.value as? RequestState.Success)?.data?.indexOfFirst { it.mediaId == mediaModel.mediaId }
            if (index != null && index != -1) {
                (_deviceAudiosList.value as? RequestState.Success)?.data?.set(index, mediaModel)
            }
            favoriteRepository.insertFavorite(mediaModel = mediaModel)
        }
    }


    fun insertPlayListSongToFavorite(path: String, isFavorite: Boolean){
        viewModelScope.launch {
            favoriteRepository.updateFavorite(path, isFavorite)
        }
    }


    fun removeAudio(media: MediaModel) {
        viewModelScope.launch {
            val currentState = _deviceAudiosList.value
            if (currentState is RequestState.Success) {
                val audioList = currentState.data as? SnapshotStateList<MediaModel>
                if (audioList != null) {
                    val wasRemoved = audioList.removeIf { it.mediaId == media.mediaId }
                    if (wasRemoved) {
                        if (favoriteRepository.isFavorite(media.path)) {
                            deleteFavorites(media)
                        }
                    }

                } else {
                    Log.e("HomeViewModel", "Data is not a SnapshotStateList!")
                }
            } else {
                Log.w("HomeViewModel", "Cannot remove audio: State is not Success")
            }
        }
    }


    fun removeFavorites(media: MediaModel) {
        deleteFavorites(media)
        val currentList = (_favorites.value as? RequestState.Success)?.data.orEmpty()
        val updatedList = currentList.filter { it.mediaId != media.mediaId }


        viewModelScope.launch {
            val updatedMedia = media.copy(isFavorite =  false)
            val index = (_deviceAudiosList.value as? RequestState.Success)?.data?.indexOfFirst { it.mediaId == updatedMedia.mediaId }
            if (index != null && index != -1) {
                (_deviceAudiosList.value as? RequestState.Success)?.data?.set(index, updatedMedia)
            }
        }


        _favorites.value = RequestState.Success(updatedList)
    }


    private fun deleteFavorites(media: MediaModel){
        viewModelScope.launch {
            favoriteRepository.unFavorite(media)
        }
    }


    private fun getAllFavorites(){
        viewModelScope.launch {
            favoriteRepository.getAllFavorites()
                .onStart {  }
                .onCompletion {  }
                .collect{ favoritesList->
                    _favorites.value = RequestState.Success(favoritesList)
                }

        }
    }


}