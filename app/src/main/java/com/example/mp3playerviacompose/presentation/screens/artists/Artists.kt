package com.example.mp3playerviacompose.presentation.screens.artists

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.mp3playerviacompose.domain.models.RequestState
import com.example.mp3playerviacompose.presentation.composable.ArtistList
import com.example.mp3playerviacompose.presentation.navigation.NavigationItems
import com.example.mp3playerviacompose.presentation.screens.Home.viewModels.HomeViewModel
import com.example.mp3playerviacompose.presentation.screens.artists.artistViewModel.ArtistViewModel

@Composable
fun Artists(onNavigateToAudioList: (String) -> Unit){
    val viewModel : ArtistViewModel = hiltViewModel()
    val artistState = viewModel.artistState.collectAsState()
    val sharedViewModel: HomeViewModel = hiltViewModel()
    LaunchedEffect(Unit) {
        viewModel.fetchArtists()
    }


    Scaffold { innerPadding ->
        Column(modifier = Modifier.consumeWindowInsets(innerPadding)) {
            when(artistState.value){
                is RequestState.Error -> {}
                RequestState.Idle -> {}
                RequestState.Loading -> {}
                is RequestState.Success -> {
                    val audioList = (artistState.value as RequestState.Success).data
                    LazyColumn(modifier = Modifier.fillMaxWidth()) {
                        items(audioList, key = { it.id }) {
                            ArtistList(it){artist->
                                sharedViewModel.albumName = null
                                sharedViewModel.artistName = artist.artistName
                                onNavigateToAudioList(NavigationItems.MusicDetails.route + "/${sharedViewModel.artistName}/${artist.artistName}")
                            }
                        }
                    }
                }
            }
        }
    }

}