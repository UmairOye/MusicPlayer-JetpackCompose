package com.example.mp3playerviacompose.data.device.mediaPlayer

import android.content.Context
import com.example.mp3playerviacompose.domain.repositories.MediaPlayerRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MediaPlayerDI {
    @Provides
    @Singleton
    fun provideMediaPlayerManager(): MediaPlayerManager {
        return MediaPlayerManager()
    }


    @Provides
    @Singleton
    fun provideMediaManagerRepository(mediaPlayerManager: MediaPlayerManager): MediaPlayerRepository{
        return mediaPlayerManager
    }
}