package com.example.mp3playerviacompose.presentation.screens.albums.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mp3playerviacompose.R
import com.example.mp3playerviacompose.domain.models.RequestState
import com.example.mp3playerviacompose.domain.utils.Constants
import com.example.mp3playerviacompose.domain.utils.hasPermission
import com.example.mp3playerviacompose.presentation.composable.NothingFound
import com.example.mp3playerviacompose.presentation.screens.albums.composable.AlbumsList
import com.example.mp3playerviacompose.presentation.navigation.NavigationItems
import com.example.mp3playerviacompose.presentation.screens.Home.viewModels.HomeViewModel
import com.example.mp3playerviacompose.presentation.screens.albums.albumViewModel.AlbumViewModel
import com.example.mp3playerviacompose.presentation.screens.artists.screen.ShowShimmerEffects

//@Preview
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun AudioAlbums(lifecycle: LifecycleOwner = LocalLifecycleOwner.current, sharedViewModel: HomeViewModel, albumViewModel: AlbumViewModel,onNavigation:(String)-> Unit){
    val albumState = albumViewModel.albumList.collectAsStateWithLifecycle()
    val context = LocalContext.current


    DisposableEffect(lifecycle) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> {
                    if(context.hasPermission(Constants.permission)){
                        albumViewModel.fetchAlbums()
                    }
                }

                else -> {}
            }
        }

        lifecycle.lifecycle.addObserver(observer)
        onDispose {
            lifecycle.lifecycle.removeObserver(observer)
            sharedViewModel.selectedMediaModel = null
        }
    }



    Scaffold { innerPadding ->
        Column(modifier = Modifier.consumeWindowInsets(innerPadding)) {
            when(albumState.value){
                is RequestState.Error -> {}

                RequestState.Idle -> {}

                RequestState.Loading -> {
                    ShowShimmerEffects()
                }

                is RequestState.Success -> {
                    val audioList = (albumState.value as RequestState.Success).data
                    if(audioList.isNotEmpty()){
                        LazyColumn(modifier = Modifier.fillMaxWidth()) {
                            items(audioList, key = { it.id }) {
                                AlbumsList(it){ album->
                                    sharedViewModel.albumName = album.albumName
                                    sharedViewModel.artistName = ""
                                    onNavigation(NavigationItems.MusicDetails.route + "//${album.albumName}/")
                                }
                            }
                        }
                    }else{
                        NothingFound(stringResource(R.string.no_albums_found))
                    }
                }
            }
        }
    }


}