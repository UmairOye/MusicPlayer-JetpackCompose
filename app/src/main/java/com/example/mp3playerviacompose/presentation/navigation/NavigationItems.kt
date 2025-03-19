package com.example.mp3playerviacompose.presentation.navigation

sealed class NavigationItems(val route: String) {
    object Home: NavigationItems(Screens.HOME.name)
    object Player: NavigationItems(Screens.PLAYER.name)
    object MusicDetails: NavigationItems(Screens.MUSIC_DETAILS.name)
}