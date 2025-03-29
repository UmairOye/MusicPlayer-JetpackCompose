package com.example.mp3playerviacompose.presentation.screens.musicDetails.screen

import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mp3playerviacompose.R
import com.example.mp3playerviacompose.domain.models.MediaModel
import com.example.mp3playerviacompose.domain.models.RequestState
import com.example.mp3playerviacompose.presentation.composable.AudioListItems
import com.example.mp3playerviacompose.presentation.composable.BackButton
import com.example.mp3playerviacompose.presentation.composable.DeleteOperation
import com.example.mp3playerviacompose.presentation.composable.ShowDeleteDialog
import com.example.mp3playerviacompose.presentation.navigation.NavigationItems
import com.example.mp3playerviacompose.presentation.screens.Home.viewModels.HomeViewModel
import com.example.mp3playerviacompose.presentation.screens.musicDetails.viewModel.MusicDetailsViewModel
import com.example.mp3playerviacompose.domain.utils.Constants
import com.example.mp3playerviacompose.domain.utils.Constants.LARGE_PADDING_1
import com.example.mp3playerviacompose.domain.utils.Utils

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
//@Preview(showSystemUi = true)
fun MusicDetailsScreen(artistName: String?, albumName: String?, folderName: String?, onNavigation:(String)-> Unit, onBackPressed:()-> Unit){
    val musicDetailsViewModel: MusicDetailsViewModel = hiltViewModel()
    val artistMusicDetails = musicDetailsViewModel.musicByArtist.collectAsStateWithLifecycle()
    val albumMusicDetails = musicDetailsViewModel.musicAlbums.collectAsStateWithLifecycle()
    val folderMusicDetails = musicDetailsViewModel.folderTracks.collectAsStateWithLifecycle()
    val sharedViewModel : HomeViewModel = hiltViewModel()
    val openAlertDialog = remember { mutableStateOf(false) }
    val performDeleteOperation = remember {
        mutableStateOf(false)
    }

    val isFileDeletedSuccessfully = remember {
        mutableStateOf(false)
    }

    val actionType = remember {
        mutableStateOf("")
    }


    Scaffold { innerPadding ->
        Column(modifier = Modifier.consumeWindowInsets(innerPadding)) {
            BackButton(heading = "Tracks", modifier = Modifier.padding(top = LARGE_PADDING_1), color = colorResource(id = R.color.back_button_color)) {
                onBackPressed()
            }


            if (performDeleteOperation.value) {
                sharedViewModel.selectedMediaModel?.let {
                    DeleteOperation(
                        performDeleteOperation = performDeleteOperation.value,
                        mediaUri = it,
                        onDeleted = {
                            performDeleteOperation.value = false
                            sharedViewModel.selectedMediaModel = null
                            isFileDeletedSuccessfully.value = true
                        },

                        onNotDeleted = {
                            performDeleteOperation.value = false
                            sharedViewModel.selectedMediaModel = null
                        })
                }
            }


            if(isFileDeletedSuccessfully.value){
                UpdateTracks(
                    viewModel = musicDetailsViewModel,
                    action = actionType.value,
                    folderName = folderName,
                    artistName = artistName,
                    albumName = albumName
                )
                isFileDeletedSuccessfully.value = false
            }


            if (openAlertDialog.value) {
                ShowDeleteDialog(openAlertDialog.value, onDismiss = {
                    openAlertDialog.value = false
                }, onConfirmation = {
                    openAlertDialog.value = false
                    performDeleteOperation.value = true
                })
            }




            Spacer(modifier = Modifier.padding(top = Constants.LARGE_PADDING))
            artistName?.let{ artistName->
                LaunchedEffect(key1 = Unit) {
                    musicDetailsViewModel.fetchSongsByArtist(artistName)
                }

                when(artistMusicDetails.value){
                    is RequestState.Error -> {}
                    RequestState.Idle -> {}
                    RequestState.Loading -> {}
                    is RequestState.Success -> {
                        actionType.value = stringResource(id = R.string.artist)
                        val audioList = (artistMusicDetails.value as RequestState.Success).data
                        SubmitList(audioList, onNavigation = { onNavigation(it) },onDeleteClicked = { mediaModel ->
                            sharedViewModel.selectedMediaModel = mediaModel
                            openAlertDialog.value = true
                        } ,sharedViewModel)
                    }
                }
            }


            albumName?.let { albumName->
                LaunchedEffect(key1 = Unit) {
                    musicDetailsViewModel.fetchAudioFilesByAlbums(albumName)
                }

                when(albumMusicDetails.value){
                    is RequestState.Error -> {}
                    RequestState.Idle -> {}
                    RequestState.Loading -> {}
                    is RequestState.Success -> {
                        actionType.value = stringResource(id = R.string.album)
                        val audioList = (albumMusicDetails.value as RequestState.Success).data
                        SubmitList(audioList, onNavigation = { onNavigation(it) },onDeleteClicked = { mediaModel ->
                            sharedViewModel.selectedMediaModel = mediaModel
                            openAlertDialog.value = true
                        }, sharedViewModel)
                    }
                }
            }


            folderName?.let{ folderName->
                LaunchedEffect(key1 = Unit) {
                    musicDetailsViewModel.fetchAudioFilesFromFolder(folderName)
                }

                when(folderMusicDetails.value){
                    is RequestState.Error -> {}
                    RequestState.Idle -> {}
                    RequestState.Loading -> {}
                    is RequestState.Success -> {
                        actionType.value = stringResource(id = R.string.folder)
                        val audioList = (folderMusicDetails.value as RequestState.Success).data
                        SubmitList(audioList, onNavigation = { onNavigation(it) },onDeleteClicked = { mediaModel ->
                            sharedViewModel.selectedMediaModel = mediaModel
                            openAlertDialog.value = true
                        } ,sharedViewModel)
                    }
                }
            }
        }
    }

}



@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun SubmitList(list: List<MediaModel>, onNavigation: (String) -> Unit,onDeleteClicked: (MediaModel) -> Unit, viewModel: HomeViewModel){
    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        items(list, key = { it.mediaId }) {
            AudioListItems(false,mediaModel = it,
                onPlayListClicked = {
                    onNavigation(NavigationItems.PlayList.route)
                },
                onFavoriteClicked = { mediaModel, isFavorite ->
                    viewModel.insertFavorite(mediaModel)
                },

                onDeleteClicked = {mediaModel ->
                onDeleteClicked(mediaModel)
                viewModel.selectedMediaModel = mediaModel
            })
            { mediaModel ->
                Utils.playerList = list.toMutableList()
                viewModel.selectedMediaModel = mediaModel
                onNavigation(NavigationItems.Player.route + "/${Uri.encode(mediaModel.path)}")
            }
        }
    }
}


@Composable
fun UpdateTracks(viewModel: MusicDetailsViewModel, action: String, folderName: String?, artistName: String?, albumName: String?){
    when(action){

        stringResource(id = R.string.folder) -> {
            LaunchedEffect(Unit) {
                if (folderName != null) {
                    viewModel.fetchAudioFilesFromFolder(folderName)
                }
            }
        }
        stringResource(id = R.string.album) -> {
            LaunchedEffect(Unit) {
                if (albumName != null) {
                    viewModel.fetchAudioFilesByAlbums(albumName)
                }
            }
        }
        stringResource(id = R.string.artist) -> {
            LaunchedEffect(Unit) {
                if(artistName!=null){
                    viewModel.fetchSongsByArtist(artistName)
                }
            }
        }
    }

}