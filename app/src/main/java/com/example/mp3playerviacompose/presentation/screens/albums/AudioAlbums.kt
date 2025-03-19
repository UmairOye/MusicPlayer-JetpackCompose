package com.example.mp3playerviacompose.presentation.screens.albums

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mp3playerviacompose.domain.models.RequestState
import com.example.mp3playerviacompose.presentation.composable.AlbumsList
import com.example.mp3playerviacompose.presentation.navigation.NavigationItems
import com.example.mp3playerviacompose.presentation.screens.Home.viewModels.HomeViewModel
import com.example.mp3playerviacompose.presentation.screens.albums.albumViewModel.AlbumViewModel

//@Preview
@Composable
fun AudioAlbums(onNavigation:(String)-> Unit){

    val albumViewModel : AlbumViewModel = hiltViewModel()
    val albumState = albumViewModel.albumList.collectAsStateWithLifecycle()
    val sharedViewModel : HomeViewModel = hiltViewModel()



    LaunchedEffect(Unit) {
        albumViewModel.fetchAlbums()
    }


    Scaffold { innerPadding ->
        Column(modifier = Modifier.consumeWindowInsets(innerPadding)) {
            when(albumState.value){
                is RequestState.Error -> {}
                RequestState.Idle -> {}
                RequestState.Loading -> {}
                is RequestState.Success -> {
                    val audioList = (albumState.value as RequestState.Success).data
                    LazyColumn(modifier = Modifier.fillMaxWidth()) {
                        items(audioList, key = { it.id }) {
                            AlbumsList(it){ album->
                                sharedViewModel.albumName = album.albumName
                                sharedViewModel.artistName = null
                                onNavigation(NavigationItems.MusicDetails.route + "/${sharedViewModel.artistName}/${sharedViewModel.albumName}")
                            }
                        }
                    }
                }
            }
        }
    }


}