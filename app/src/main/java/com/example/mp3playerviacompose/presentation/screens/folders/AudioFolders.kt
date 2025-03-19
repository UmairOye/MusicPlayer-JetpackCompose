package com.example.mp3playerviacompose.presentation.screens.folders

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
import com.example.mp3playerviacompose.presentation.composable.FolderListItems
import com.example.mp3playerviacompose.presentation.navigation.NavigationItems
import com.example.mp3playerviacompose.presentation.screens.Home.viewModels.HomeViewModel
import com.example.mp3playerviacompose.presentation.screens.folders.folderViewModel.FolderViewModel

@Composable
fun AudioFolders(onNavigation:(String)-> Unit){

    val folderViewModel : FolderViewModel = hiltViewModel()
    val sharedViewModel: HomeViewModel = hiltViewModel()
    val folderState = folderViewModel.audioFolderList.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        folderViewModel.fetchAudioFolders()
    }


    Scaffold { innerPadding ->
        Column(modifier = Modifier.consumeWindowInsets(innerPadding)) {
            when(folderState.value){
                is RequestState.Error -> {}
                RequestState.Idle -> {}
                RequestState.Loading -> {}
                is RequestState.Success -> {
                    val folderList = (folderState.value as RequestState.Success).data
                    LazyColumn(modifier = Modifier.fillMaxWidth()) {
                        items(folderList, key = { it.displayName}) {
                            FolderListItems(it){folderItem->
                                sharedViewModel.folderName = folderItem.displayName
                                onNavigation(NavigationItems.MusicDetails.route + "/${sharedViewModel.artistName}/${sharedViewModel.artistName}/${sharedViewModel.folderName}")
                            }
                        }
                    }
                }
            }
        }
    }
}