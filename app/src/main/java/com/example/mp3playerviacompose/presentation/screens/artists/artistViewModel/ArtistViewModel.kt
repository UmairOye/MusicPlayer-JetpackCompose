package com.example.mp3playerviacompose.presentation.screens.artists.artistViewModel


import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mp3playerviacompose.domain.models.Artist
import com.example.mp3playerviacompose.domain.models.RequestState
import com.example.mp3playerviacompose.domain.repositories.MediaFetchingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@HiltViewModel
class ArtistViewModel @Inject constructor(private val mediaFetchingRepository: MediaFetchingRepository,
    @ApplicationContext context: Context): ViewModel() {
    private val _artistState = MutableStateFlow<RequestState<List<Artist>>>(RequestState.Idle)
    val artistState: StateFlow<RequestState<List<Artist>>> = _artistState

    fun fetchArtists(){
        viewModelScope.launch {

            mediaFetchingRepository.getArtists().onStart {  }
                .onCompletion {  }
                .collect{ artistList ->
                    _artistState.value = artistList
                }
        }
    }


    fun changeStates(){
        _artistState.value = RequestState.Idle
    }

}