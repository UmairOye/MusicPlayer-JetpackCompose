package com.example.mp3playerviacompose.domain.utils

import android.Manifest
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.ui.unit.dp

object Constants {

    //Preference Constants
    const val PREF_NAME = "PLAYER_PREFERENCE"
    const val SEND_USER_TO_SETTINGS = "SEND_USER_TO_SETTINGS"
    const val IS_FIRST_TIME_ARTIST = "IS_FIRST_TIME_ARTIST"
    const val IS_FIRST_TIME_ALBUM = "IS_FIRST_TIME_ALBUM"


    //Bottom Sheet Constants
    val SHEET_TOP_PADDING = 20.dp
    val TONAL_ELEVATION = 5.dp
    val HANDLER_WIDTH = 50.dp
    val HANDLER_HEIGHT = 4.dp
    val BUTTON_HEIGHT = 45.dp
    val BUTTON_ROUNDNESS = 10.dp


    //Permission manifest
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    val permission = if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.S) {
        Manifest.permission.READ_EXTERNAL_STORAGE
    } else {
        Manifest.permission.READ_MEDIA_AUDIO
    }


    //rational constants
    val MEDIUM_PADDING = 10.dp
    val LARGE_PADDING =  20.dp
    val LARGE_PADDING_1 =  32.dp
    val EXTRA_LARGE_PADDING =  30.dp
    val SMALL_PADDING = 4.dp
    val MEDIUM_PADDING_1 = 8.dp

    //player screen constants

    val MUSIC_ICON_SIZE = 300.dp
    val COMPONENT_PADDING = 30.dp

    //divider constants
    val DIVIDER_HEIGHT = 4.dp
    val DIVIDER_PADDING = 28.dp


}