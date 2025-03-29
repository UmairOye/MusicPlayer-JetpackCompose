package com.example.mp3playerviacompose.data.databases.playListRepository

import com.example.mp3playerviacompose.data.databases.dao.PlayListDao
import com.example.mp3playerviacompose.domain.models.PlayListEntity
import com.example.mp3playerviacompose.domain.models.SongsPlayList
import com.example.mp3playerviacompose.domain.repositories.PlayListRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PlayListRepositoryImplementation @Inject constructor(private val dao: PlayListDao): PlayListRepository {
    override suspend fun createPlayList(playListEntity: PlayListEntity) = withContext(Dispatchers.IO){
        dao.createPlayList(playListEntity)
    }

    override suspend fun getPlayList(): Flow<List<PlayListEntity>> = withContext(Dispatchers.IO){
        return@withContext dao.getPlayList()
    }

    override suspend fun deletePlayList(playListEntity: PlayListEntity) = withContext(Dispatchers.IO){
        dao.deletePlayList(playListEntity)
    }

    override suspend fun insertSongsIntoPlayList(songsPlayList: SongsPlayList) = withContext(Dispatchers.IO) {
        if(dao.doesPathExist(songsPlayList.path).not()){
            dao.insertSongsIntoPlayList(songsPlayList)
            val songsDuration = dao.getTotalSongsCountForPlaylist(songsPlayList.playListName!!)
            val songsCount = dao.getPlaylistCount(songsPlayList.playListName!!)
            dao.updateSongDetails(songsPlayList.playListName!!, songsCount, songsDuration!!)
        }
    }

    override suspend fun getSongsFromPlayList(): Flow<List<SongsPlayList>> = withContext(Dispatchers.IO){
        return@withContext dao.getPlaylistSongs()
    }



    override suspend fun deleteFromPlaylist(songsPlayList: SongsPlayList) = withContext(Dispatchers.IO){
        val playlistName = songsPlayList.playListName ?: return@withContext

        dao.deleteSongFromPlaylist(songsPlayList)
        val songsDuration = dao.getTotalSongsCountForPlaylist(playlistName) ?: 0
        val songsCount = dao.getPlaylistCount(playlistName) ?: 0
        dao.updateSongDetails(playlistName, songsCount, songsDuration)
    }

    override suspend fun updatePlayListFavorite(path: String, isFavorite: Boolean) = withContext(Dispatchers.IO){
        dao.updatePlayListFavorite(path, isFavorite)
    }

}