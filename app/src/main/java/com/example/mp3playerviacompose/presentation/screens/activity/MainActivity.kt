package com.example.mp3playerviacompose.presentation.screens.activity

import android.graphics.Color.TRANSPARENT
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.mp3playerviacompose.presentation.navigation.AppNavHost
import com.example.mp3playerviacompose.presentation.screens.Home.viewModels.HomeViewModel
import com.example.mp3playerviacompose.presentation.screens.albums.albumViewModel.AlbumViewModel
import com.example.mp3playerviacompose.presentation.screens.artists.artistViewModel.ArtistViewModel
import com.example.mp3playerviacompose.presentation.screens.playList.viewModel.PlayListViewModel
import com.example.mp3playerviacompose.presentation.theme.Mp3PlayerViaComposeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val homeViewModel : HomeViewModel by viewModels()
    private val artistViewModel : ArtistViewModel by viewModels()
    private val albumViewModel : AlbumViewModel by viewModels()
    private val playListViewModel : PlayListViewModel by viewModels()




    private lateinit var navController: NavHostController
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val lightTransparentStyle = SystemBarStyle.light(
            scrim = TRANSPARENT,
            darkScrim = TRANSPARENT
        )
        enableEdgeToEdge(
            statusBarStyle = lightTransparentStyle,
            navigationBarStyle = lightTransparentStyle
        )

        setContent {
            Mp3PlayerViaComposeTheme {
                navController = rememberNavController()
                AppNavHost(
                    navController = navController,
                    homeViewModel =  homeViewModel,
                    albumViewModel = albumViewModel,
                    artistViewModel = artistViewModel,
                    playListViewModel = playListViewModel
                )
            }
        }
    }
}

