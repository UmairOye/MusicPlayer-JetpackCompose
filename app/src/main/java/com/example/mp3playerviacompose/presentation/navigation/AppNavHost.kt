package com.example.mp3playerviacompose.presentation.navigation

import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.mp3playerviacompose.presentation.screens.Home.HomeScreen
import com.example.mp3playerviacompose.presentation.screens.Home.viewModels.HomeViewModel
import com.example.mp3playerviacompose.presentation.screens.albums.albumViewModel.AlbumViewModel
import com.example.mp3playerviacompose.presentation.screens.artists.artistViewModel.ArtistViewModel
import com.example.mp3playerviacompose.presentation.screens.favorite.screen.FavoriteScreen
import com.example.mp3playerviacompose.presentation.screens.musicDetails.screen.MusicDetailsScreen
import com.example.mp3playerviacompose.presentation.screens.playList.screen.PlayListScreen
import com.example.mp3playerviacompose.presentation.screens.playList.screen.PlayListSongs
import com.example.mp3playerviacompose.presentation.screens.playList.viewModel.PlayListViewModel
import com.example.mp3playerviacompose.presentation.screens.player.playerScreen.PlayerScreen


@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun AppNavHost(
    navController: NavHostController,
    homeViewModel: HomeViewModel,
    albumViewModel: AlbumViewModel,
    artistViewModel: ArtistViewModel,
    playListViewModel: PlayListViewModel,
) {


    NavHost(navController = navController, startDestination = NavigationItems.Home.route) {
        composable(route = NavigationItems.Home.route,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start,
                    animationSpec = tween(700)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.End,
                    animationSpec = tween(700)
                )

            }) {
            HomeScreen(
                homeViewModel,
                artistViewModel,
                albumViewModel,
                onNavigation = { route -> navController.navigate(route) }
            )
        }


        composable(route = NavigationItems.Favorites.route,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Up,
                    animationSpec = tween(700)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Down,
                    animationSpec = tween(700)
                )

            }) {
            FavoriteScreen(
                homeViewModel = homeViewModel,
                onNavigation = { route -> navController.navigate(route) },
                onBackPressed = { navController.popBackStack() }

            )
        }

        composable(
            route = NavigationItems.Player.route + "/{mediaPath}",
            arguments = listOf(navArgument("mediaPath") { type = NavType.StringType }),
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start,
                    animationSpec = tween(700)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.End,
                    animationSpec = tween(700)
                )

            }
        ) {
            val mediaPath =
                it.arguments?.getString("mediaPath")?.let { mediaPath -> Uri.decode(mediaPath) }

            if (mediaPath != null)
                PlayerScreen(
                    mediaPath,
                    onNavigation = { route -> navController.navigate(route) },
                    onBackPressed = { navController.popBackStack() }
                )
        }


        composable(route = NavigationItems.MusicDetails.route + "/{artistName}/{albumName}/{folderName}",
            arguments = listOf(
                navArgument("artistName") {
                    type = NavType.StringType
                },

                navArgument("albumName") {
                    type = NavType.StringType
                },
                navArgument("folderName") {
                    type = NavType.StringType
                }
            )) {


            val artistName = it.arguments?.getString("artistName")
            val albumName = it.arguments?.getString("albumName")
            val folderName = it.arguments?.getString("folderName")

            MusicDetailsScreen(
                artistName = artistName,
                albumName = albumName,
                folderName = folderName,
                onNavigation = { route -> navController.navigate(route) },
                onBackPressed = { navController.popBackStack() }
            )
        }

        composable(route = NavigationItems.PlayList.route,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start,
                    animationSpec = tween(700)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.End,
                    animationSpec = tween(700)
                )

            }) {

            PlayListScreen(
                onBackPressed = { navController.popBackStack() },
                playListViewModel = playListViewModel,
                sharedViewModel = homeViewModel,
                onNavigation = {route -> navController.navigate(route)}
            )
        }


        composable(route = NavigationItems.PlayListSongs.route + "/{playListName}",
            arguments = listOf(
                navArgument("playListName") {
                    type = NavType.StringType
                }
            ),
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(700)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.End,
                    animationSpec = tween(700)
                )
            }) {

            val playListName = it.arguments?.getString("playListName") ?: ""
            PlayListSongs(
                playListName = playListName,
                onBackPressed = { navController.popBackStack() },
                onNavigation = { route -> navController.navigate(route) },
                playListViewModel = playListViewModel,
                homeViewModel
            )
        }

    }
}
