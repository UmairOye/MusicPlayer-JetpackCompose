package com.example.mp3playerviacompose.data.databases.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.mp3playerviacompose.domain.models.MediaModel
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {

    @Insert
    fun insertFavorite(mediaModel: MediaModel)

    @Query("SELECT * FROM tbl_favorite")
    fun getAllFavorites(): Flow<List<MediaModel>>

    @Query("SELECT EXISTS(SELECT 1 FROM tbl_favorite WHERE path = :path)")
    fun doesPathExist(path: String): Boolean


    @Delete
    fun deletePath(mediaModel: MediaModel)

    @Query("UPDATE tbl_favorite SET isFavorite = :isFavorite WHERE path = :path")
    fun updateFavorites(path: String, isFavorite: Boolean)

}