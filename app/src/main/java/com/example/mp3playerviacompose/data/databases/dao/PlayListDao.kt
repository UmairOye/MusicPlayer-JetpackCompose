package com.example.mp3playerviacompose.data.databases.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.mp3playerviacompose.domain.models.PlayListEntity
import com.example.mp3playerviacompose.domain.models.SongsPlayList
import kotlinx.coroutines.flow.Flow

@Dao
interface PlayListDao {

    @Insert
    fun createPlayList(playListEntity: PlayListEntity)

    @Query("SELECT * FROM tbl_playlist")
    fun getPlayList(): Flow<List<PlayListEntity>>

    @Delete
    fun deletePlayList(playListEntity: PlayListEntity)

    @Update
    fun updatePlayList(playListEntity: PlayListEntity)


    @Insert
    fun insertSongsIntoPlayList(songsPlayList: SongsPlayList)


    @Query("Select * from tbl_songsPlaylist")
    fun getPlaylistSongs(): Flow<List<SongsPlayList>>

    @Query("SELECT COUNT(*) FROM tbl_songsPlaylist WHERE playListName = :playlistName")
    suspend fun getPlaylistCount(playlistName: String): Int

    @Query("SELECT EXISTS(SELECT 1 FROM tbl_songsPlaylist WHERE path = :path)")
    fun doesPathExist(path: String): Boolean

    @Query("UPDATE tbl_playList SET songsCount = :songsCount, songDuration = :songDuration WHERE playListName = :playListName")
    suspend fun updateSongDetails(playListName: String, songsCount: Int, songDuration: Long)


    @Query("SELECT SUM(duration) FROM tbl_songsPlaylist WHERE playListName = :playListName")
    suspend fun getTotalSongsCountForPlaylist(playListName: String): Long?


    @Delete
    suspend fun deleteSongFromPlaylist(songsPlayList: SongsPlayList)

    @Query("UPDATE tbl_songsPlaylist SET isFavorite = :isFavorite WHERE path = :path")
    suspend fun updatePlayListFavorite(path: String, isFavorite: Boolean)

}