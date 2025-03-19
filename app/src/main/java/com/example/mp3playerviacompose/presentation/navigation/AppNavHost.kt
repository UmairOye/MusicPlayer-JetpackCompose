package com.example.mp3playerviacompose.presentation.navigation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.mp3playerviacompose.presentation.screens.Home.HomeScreen
import com.example.mp3playerviacompose.presentation.screens.musicDetails.MusicDetailsScreen
import com.example.mp3playerviacompose.presentation.screens.player.playerScreen.PlayerScreen

@Composable
fun AppNavHost(navController: NavHostController){


    NavHost(navController = navController, startDestination = NavigationItems.Home.route) {

        composable(route = NavigationItems.Home.route){
            HomeScreen(
                onNavigation = {route -> navController.navigate(route)},
                onBackPressed =  {navController.popBackStack()}
            )
        }


        composable(route = NavigationItems.Player.route+ "/{mediaPath}",arguments = listOf(navArgument("mediaPath") { type = NavType.StringType })){
            val mediaPath = it.arguments?.getString("mediaPath")?.let {mediaPath-> Uri.decode(mediaPath) }

            if(mediaPath!=null)
            PlayerScreen(
                mediaPath,
                onNavigation = {route-> navController.navigate(route)},
                onBackPressed = {navController.popBackStack()}
            )
        }


        composable(route = NavigationItems.MusicDetails.route+"/{artistName}/{albumName}/{folderName}", arguments = listOf(
            navArgument("artistName"){
                type = NavType.StringType
            },

            navArgument("albumName"){
                type = NavType.StringType
            },
            navArgument("folderName"){
                type = NavType.StringType
            }
        )){
            val artistName = it.arguments?.getString("artistName")
            val albumName = it.arguments?.getString("albumName")
            val folderName = it.arguments?.getString("folderName")

            MusicDetailsScreen(
                artistName = artistName,
                albumName = albumName,
                folderName = folderName,
                onNavigation = {route-> navController.navigate(route)},
                onBackPressed = {navController.popBackStack()}
            )
        }
    }

}