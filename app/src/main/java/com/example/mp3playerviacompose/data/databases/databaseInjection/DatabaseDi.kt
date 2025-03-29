package com.example.mp3playerviacompose.data.databases.databaseInjection

import android.content.Context
import androidx.room.Room
import com.example.mp3playerviacompose.data.databases.dao.FavoriteDao
import com.example.mp3playerviacompose.data.databases.dao.PlayListDao
import com.example.mp3playerviacompose.data.databases.database.MusicDatabase
import com.example.mp3playerviacompose.data.databases.favoriteRepository.FavoriteRepositoryImplementation
import com.example.mp3playerviacompose.data.databases.playListRepository.PlayListRepositoryImplementation
import com.example.mp3playerviacompose.domain.repositories.FavoriteRepository
import com.example.mp3playerviacompose.domain.repositories.PlayListRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseDi {

    @Provides
    @Singleton
    fun provideMusicDatabase(@ApplicationContext context: Context): MusicDatabase {
        return Room.databaseBuilder(context, MusicDatabase::class.java, "music_database")
            .fallbackToDestructiveMigration().build()
    }


    @Provides
    @Singleton
    fun provideFavoriteDao(musicDatabase: MusicDatabase): FavoriteDao {
        return musicDatabase.favDao()
    }


    @Provides
    @Singleton
    fun providePlayListDao(musicDatabase: MusicDatabase): PlayListDao {
        return musicDatabase.playListDao()
    }

    @Provides
    @Singleton
    fun bindPlayListRepository(playListRepositoryImplementation: PlayListRepositoryImplementation): PlayListRepository {
        return playListRepositoryImplementation
    }


    @Provides
    @Singleton
    fun bindFavoriteRepository(favoriteRepositoryImplementation: FavoriteRepositoryImplementation): FavoriteRepository{
        return favoriteRepositoryImplementation
    }


}