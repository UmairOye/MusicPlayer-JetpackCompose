package com.example.mp3playerviacompose.presentation.app

import android.app.Application
import com.example.mp3playerviacompose.data.local.SharedPref
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MP3PlayerApp: Application() {
    override fun onCreate() {
        super.onCreate()
        SharedPref.init(this)
    }
}