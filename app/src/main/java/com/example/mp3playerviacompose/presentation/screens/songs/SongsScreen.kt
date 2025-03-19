package com.example.mp3playerviacompose.presentation.screens.songs

import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.mp3playerviacompose.domain.models.MediaModel
import com.example.mp3playerviacompose.domain.models.RequestState
import com.example.mp3playerviacompose.presentation.composable.AskedPermission
import com.example.mp3playerviacompose.presentation.composable.AudioListItems
import com.example.mp3playerviacompose.presentation.composable.DeleteOperation
import com.example.mp3playerviacompose.presentation.composable.RationalPermission
import com.example.mp3playerviacompose.presentation.composable.ShimmerEffect
import com.example.mp3playerviacompose.presentation.composable.ShowBottomSheet
import com.example.mp3playerviacompose.presentation.composable.ShowDeleteDialog
import com.example.mp3playerviacompose.presentation.navigation.NavigationItems
import com.example.mp3playerviacompose.presentation.screens.Home.viewModels.HomeViewModel
import com.example.mp3playerviacompose.domain.utils.Constants
import com.example.mp3playerviacompose.domain.utils.Utils
import com.example.mp3playerviacompose.domain.utils.hasPermission

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SongsScreen(
    onNavigation: (String) -> Unit,
    onBackPressed: () -> Unit,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
) {

    val homeViewModel: HomeViewModel = hiltViewModel()
    val mediaState = homeViewModel.deviceAudioList.collectAsState()
    var isBottomSheetVisible by rememberSaveable { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    val openAlertDialog = remember { mutableStateOf(false) }
    val performDeleteOperation = remember {
        mutableStateOf(false)
    }

    var isPermissionCalled by remember {
        mutableStateOf(false)
    }

    val context = LocalContext.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> {
                    isPermissionCalled = false
                    isBottomSheetVisible = !context.hasPermission(Constants.permission)
                    if (context.hasPermission(Constants.permission)) {
                        homeViewModel.fetchAudioFiles()
                    }
                }

                else -> {}
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            homeViewModel.selectedMediaModel = null
        }
    }


    Scaffold { innerPadding ->
        Column(modifier = Modifier.consumeWindowInsets(innerPadding)) {
            if (isBottomSheetVisible) {
                ShowBottomSheet(isBottomSheetVisible = true,
                    sheetState = sheetState,
                    onDismiss = {
                        isBottomSheetVisible = false
                    }, onRequestPermission = {
                        isBottomSheetVisible = false
                        isPermissionCalled = true
                    })
            }



            if (isPermissionCalled) {
                AskedPermission(true) { isPermissionGranted ->
                    isPermissionCalled = false
                    when (isPermissionGranted) {
                        true -> {
                            homeViewModel.fetchAudioFiles()
                        }

                        false -> {
                            isBottomSheetVisible = true
                        }
                    }
                }
            }


            if (!isBottomSheetVisible && context.hasPermission(Constants.permission).not()) {
                RationalPermission {
                    isBottomSheetVisible = true
                }
            }


            if (performDeleteOperation.value) {
                homeViewModel.selectedMediaModel?.let {
                    DeleteOperation(
                        performDeleteOperation = performDeleteOperation.value,
                        mediaUri = it,

                        onDeleted = {
                            performDeleteOperation.value = false
                            homeViewModel.selectedMediaModel = null
                            homeViewModel.fetchAudioFiles()
                        },

                        onNotDeleted = {
                            performDeleteOperation.value = false
                            homeViewModel.selectedMediaModel = null
                        })
                }
            }


            if (openAlertDialog.value) {
                ShowDeleteDialog(openAlertDialog.value, onDismiss = {
                    openAlertDialog.value = false
                }, onConfirmation = {
                    openAlertDialog.value = false
                    performDeleteOperation.value = true
                })
            }





            when (mediaState.value) {
                is RequestState.Error -> {}
                RequestState.Idle -> {}
                RequestState.Loading -> {
                    ShowList(
                        list = homeViewModel.getLoadingList(),
                        isLoading = true,
                        homeViewModel,
                        onDeleteClicked = {}) {

                    }
                }

                is RequestState.Success -> {
                    val audioList = (mediaState.value as RequestState.Success).data
                    ShowList(list = audioList, isLoading = false, homeViewModel,
                        onDeleteClicked = { mediaModel ->
                            homeViewModel.selectedMediaModel = mediaModel
                            openAlertDialog.value = true
                        })
                    {
                        onNavigation(it)
                    }
                }
            }
        }
    }
}


@Composable
fun ShowList(
    list: List<MediaModel>,
    isLoading: Boolean = true,
    viewModel: HomeViewModel,
    onDeleteClicked: (MediaModel) -> Unit,
    onNavigation: (String) -> Unit,
) {
    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        items(list, key = { it.mediaId }) {
            if (isLoading) {
                ShimmerEffect()
            } else {
                AudioListItems(mediaModel = it, onDeleteClicked = { mediaModel ->
                    onDeleteClicked(mediaModel)
                    viewModel.selectedMediaModel = mediaModel
                }) { mediaModel ->
                    Utils.playerList = list.toMutableList()
                    viewModel.selectedMediaModel = mediaModel
                    onNavigation(NavigationItems.Player.route + "/${Uri.encode(mediaModel.path)}")
                }
            }
        }
    }

}