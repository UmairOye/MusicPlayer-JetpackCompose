package com.example.mp3playerviacompose.presentation.screens.artists.screen

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mp3playerviacompose.R
import com.example.mp3playerviacompose.data.local.SharedPref
import com.example.mp3playerviacompose.domain.models.RequestState
import com.example.mp3playerviacompose.domain.utils.Constants
import com.example.mp3playerviacompose.domain.utils.Utils
import com.example.mp3playerviacompose.domain.utils.hasPermission
import com.example.mp3playerviacompose.presentation.composable.NothingFound
import com.example.mp3playerviacompose.presentation.composable.ShimmerEffect
import com.example.mp3playerviacompose.presentation.screens.artists.composable.ArtistList
import com.example.mp3playerviacompose.presentation.navigation.NavigationItems
import com.example.mp3playerviacompose.presentation.screens.Home.viewModels.HomeViewModel
import com.example.mp3playerviacompose.presentation.screens.artists.artistViewModel.ArtistViewModel

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun Artists(artistViewModel: ArtistViewModel,
            homeViewModel: HomeViewModel,
            onNavigateToAudioList: (String) -> Unit,
            lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current){

    val context = LocalContext.current
    val artistState = artistViewModel.artistState.collectAsState()


    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> {
                    if(context.hasPermission(Constants.permission)){
                        artistViewModel.fetchArtists()
                    }
                }

                else -> {}
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
//            homeViewModel.selectedMediaModel = null
        }
    }


    Scaffold { innerPadding ->
        Column(modifier = Modifier.consumeWindowInsets(innerPadding)) {
            when(artistState.value){
                is RequestState.Error -> {
                }
                RequestState.Idle -> {
                }
                RequestState.Loading -> {
                    ShowShimmerEffects()
                }

                is RequestState.Success -> {
                    val audioList = (artistState.value as RequestState.Success).data
                    if(audioList.isNotEmpty()){
                        LazyColumn(modifier = Modifier.fillMaxWidth()) {
                            items(audioList, key = { it.id }) {
                                ArtistList(it){artist->
                                    homeViewModel.albumName = null
                                    homeViewModel.artistName = artist.artistName
                                    onNavigateToAudioList(NavigationItems.MusicDetails.route + "/${homeViewModel.artistName}//")
                                }
                            }
                        }
                    }else{
                        NothingFound(stringResource(R.string.no_artists_found))
                    }
                }
            }
        }
    }
}

@Composable
fun ShowShimmerEffects(){
    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        items(6) {
            ShimmerEffect()
        }

    }
}