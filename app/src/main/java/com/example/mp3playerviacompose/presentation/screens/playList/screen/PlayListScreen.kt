package com.example.mp3playerviacompose.presentation.screens.playList.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mp3playerviacompose.R
import com.example.mp3playerviacompose.domain.models.RequestState
import com.example.mp3playerviacompose.domain.utils.Constants.LARGE_PADDING_1
import com.example.mp3playerviacompose.domain.utils.Constants.MEDIUM_PADDING
import com.example.mp3playerviacompose.presentation.composable.BackButton
import com.example.mp3playerviacompose.presentation.composable.NothingFound
import com.example.mp3playerviacompose.presentation.composable.PlayListDialog
import com.example.mp3playerviacompose.presentation.composable.ShimmerEffect
import com.example.mp3playerviacompose.presentation.composable.ThemePreviews
import com.example.mp3playerviacompose.presentation.navigation.NavigationItems
import com.example.mp3playerviacompose.presentation.screens.Home.viewModels.HomeViewModel
import com.example.mp3playerviacompose.presentation.screens.playList.composables.PlayListItems
import com.example.mp3playerviacompose.presentation.screens.playList.viewModel.PlayListViewModel

@Composable
fun PlayListScreen(
    onBackPressed: () -> Unit,
    onNavigation:(String) -> Unit,
    playListViewModel: PlayListViewModel,
    sharedViewModel: HomeViewModel,
    lifecycleOwner: androidx.lifecycle.LifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
) {

    val playListState = playListViewModel.allPlaylist.collectAsStateWithLifecycle()
    val isAlertDialogVisible = remember {
        mutableStateOf(false)
    }

    var wantToAddSong by remember { mutableStateOf(sharedViewModel.wantToAddPlayList) }
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
            sharedViewModel.wantToAddPlayList = false
            sharedViewModel.selectedMediaModel = null
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }


    Scaffold { innerPadding ->
        Column(modifier = Modifier
            .consumeWindowInsets(innerPadding)
            .padding(bottom = LARGE_PADDING_1)) {
            BackButton(
                heading = stringResource(R.string.all_playlist),
                modifier = Modifier.padding(top = LARGE_PADDING_1),
                color = colorResource(id = R.color.back_button_color)
            ) {
                onBackPressed()
            }

            CreateNewPlayList{
                isAlertDialogVisible.value = true
            }

            if(isAlertDialogVisible.value){
                PlayListDialog(isAlertDialogVisible = isAlertDialogVisible.value,
                    onDialogDismiss = {
                        isAlertDialogVisible.value = false
                    },
                    onConfirmation = { playListName->
                        isAlertDialogVisible.value = false
                        playListViewModel.createPlayList(playListName)
                    })
            }


            when (playListState.value) {
                is RequestState.Error -> {}
                RequestState.Idle -> {
                    ShimmerEffect()
                }

                RequestState.Loading -> {
                    ShimmerEffect()
                }

                is RequestState.Success -> {
                    val playList = (playListState.value as RequestState.Success).data
                    if (playList.isNotEmpty()) {
                        LazyColumn(modifier = Modifier
                            .fillMaxWidth()
                            .weight(.9f)
                            .padding(top = MEDIUM_PADDING)) {
                            items(playList,key = {it.id}){ playListModel ->
                                PlayListItems(wantToAddSongs = wantToAddSong, playListModel,
                                    onDelete = {playList -> playListViewModel.deletePlayList(playList)},
                                    playListUnchecked = {playListViewModel.removeFromPlayList(it)},
                                    playListChecked = { playListViewModel.makePlayList(it) },
                                    onClicked = {playListName -> onNavigation(NavigationItems.PlayListSongs.route + "/$playListName")},
                                    onAddTracks = {

                                    }
                                )
                            }
                        }


                        if(wantToAddSong){
                            Button(onClick = {
                                playListViewModel.insertSongIntoPlayList(sharedViewModel.selectedMediaModel)
                                wantToAddSong = false
                            }, modifier = Modifier
                                .fillMaxWidth()
                                .weight(.088f)
                                .height(42.dp)
                                .padding(all = MEDIUM_PADDING),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = colorResource(id = R.color.sky_blue)
                                ), shape = RoundedCornerShape(MEDIUM_PADDING)
                            ) {

                                Text(text = stringResource(R.string.add_and_save),
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 16.sp)
                            }
                        }
                    } else {
                        NothingFound(stringResource(R.string.no_playlist_found))
                    }
                }
            }
        }
    }
}



@ThemePreviews
@Composable
fun CreateNewPlayList(onClicked: () -> Unit = {}){
    Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(start = MEDIUM_PADDING, top = MEDIUM_PADDING)
            .clickable {
                onClicked()
            }) {
        Icon(imageVector = Icons.Default.Add, contentDescription = stringResource(R.string.new_playlist))
        Text(text = stringResource(R.string.create_a_new_playlist),
            fontWeight = FontWeight.Medium,
            fontSize = 15.sp,
            style = TextStyle(MaterialTheme.colorScheme.onBackground),
            modifier = Modifier.padding(start = MEDIUM_PADDING)
        )
    }
}