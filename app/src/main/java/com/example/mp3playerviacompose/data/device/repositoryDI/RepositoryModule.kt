package com.example.mp3playerviacompose.data.device.repositoryDI

import com.example.mp3playerviacompose.data.device.repositories.MediaFetchRepositoryImplementation
import com.example.mp3playerviacompose.domain.repositories.MediaFetchingRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {


    @Provides
    @Singleton
    fun provideMediaFetchingRepository(mediaFetchRepositoryImplementation: MediaFetchRepositoryImplementation): MediaFetchingRepository {
        return mediaFetchRepositoryImplementation
    }

}