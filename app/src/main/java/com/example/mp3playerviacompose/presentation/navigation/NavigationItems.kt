package com.example.mp3playerviacompose.presentation.navigation

sealed class NavigationItems(val route: String) {
    object Home: NavigationItems(Screens.HOME.name)
    object Player: NavigationItems(Screens.PLAYER.name)
    object MusicDetails: NavigationItems(Screens.MUSIC_DETAILS.name)
    object Favorites: NavigationItems(Screens.FAVORITES.name)
    object PlayList : NavigationItems(Screens.PLAYLIST.name)
    object PlayListSongs: NavigationItems(Screens.SONGS_PLAYLIST.name)
}