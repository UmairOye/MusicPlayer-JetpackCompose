package com.example.mp3playerviacompose.domain.utils

import android.util.Log
import com.example.mp3playerviacompose.BuildConfig
import com.example.mp3playerviacompose.domain.models.MediaModel

object Utils {
    fun printLog(message: String, tag: String = "Mp3PlayerTag"){
        if(BuildConfig.DEBUG){
            Log.d(tag, message)
        }
    }


    var playerList = mutableListOf<MediaModel>()

}