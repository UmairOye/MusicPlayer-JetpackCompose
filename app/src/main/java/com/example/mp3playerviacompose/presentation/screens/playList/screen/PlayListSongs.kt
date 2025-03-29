package com.example.mp3playerviacompose.presentation.screens.playList.screen

import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mp3playerviacompose.R
import com.example.mp3playerviacompose.domain.models.RequestState
import com.example.mp3playerviacompose.domain.models.SongsPlayList
import com.example.mp3playerviacompose.domain.utils.Constants.LARGE_PADDING_1
import com.example.mp3playerviacompose.presentation.composable.BackButton
import com.example.mp3playerviacompose.presentation.composable.ShimmerEffect
import com.example.mp3playerviacompose.presentation.navigation.NavigationItems
import com.example.mp3playerviacompose.presentation.screens.Home.viewModels.HomeViewModel
import com.example.mp3playerviacompose.presentation.screens.playList.composables.PlayListSongsItems
import com.example.mp3playerviacompose.presentation.screens.playList.viewModel.PlayListViewModel

@Composable
fun PlayListSongs(
    playListName: String,
    onBackPressed:()-> Unit,
    onNavigation:(String) -> Unit,
    playListViewModel: PlayListViewModel,
    homeViewModel: HomeViewModel,
    lifecycleOwner: androidx.lifecycle.LifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
) {

    val allPlayListSongs = playListViewModel.allPlaylistSongs.collectAsStateWithLifecycle()

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> {
                }

                else -> {}
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }



    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .consumeWindowInsets(innerPadding)
                .padding(bottom = LARGE_PADDING_1)
        ) {
            BackButton(
                heading = playListName,
                modifier = Modifier.padding(top = LARGE_PADDING_1),
                color = colorResource(id = R.color.back_button_color)
            ) {
                onBackPressed()
            }


            when(allPlayListSongs.value){
                is RequestState.Error -> {}
                RequestState.Idle -> {}
                RequestState.Loading -> ShimmerEffect()
                is RequestState.Success -> {
                    val songs = (allPlayListSongs.value as RequestState.Success<List<SongsPlayList>>).data
                    LazyColumn(modifier = Modifier.fillMaxWidth()) {
                        items(songs, key = {it.id }){ playListSongs->
                            PlayListSongsItems(
                                isFavorite = playListSongs.isFavorite,
                                mediaModel = playListSongs,
                                onFavoriteClicked = {playListModel, isFavorite->
                                    homeViewModel.insertPlayListSongToFavorite(playListModel.path, isFavorite)
                                    playListViewModel.updatePlayListFavorites(playListModel.path, isFavorite)
                                },
                                onDeleteClicked = { deletePlayList->
                                    playListViewModel.deleteFromPlayList(deletePlayList)
                                },
                                onItemClicked = { mediaModel ->
                                    playListViewModel.addSongsToPlaylist(songs)
                                    onNavigation(NavigationItems.Player.route + "/${Uri.encode(mediaModel.path)}")
                                }
                            )
                        }
                    }
                }
            }
        }
    }


}