package com.example.mp3playerviacompose.domain.repositories

import com.example.mp3playerviacompose.domain.models.PlayListEntity
import com.example.mp3playerviacompose.domain.models.SongsPlayList
import kotlinx.coroutines.flow.Flow

interface PlayListRepository {
    suspend fun createPlayList(playListEntity: PlayListEntity)
    suspend fun getPlayList(): Flow<List<PlayListEntity>>
    suspend fun deletePlayList(playListEntity: PlayListEntity)

    suspend fun insertSongsIntoPlayList(songsPlayList: SongsPlayList)
    suspend fun getSongsFromPlayList(): Flow<List<SongsPlayList>>
    suspend fun deleteFromPlaylist(songsPlayList: SongsPlayList)
    suspend fun updatePlayListFavorite(path: String, isFavorite: Boolean)
}