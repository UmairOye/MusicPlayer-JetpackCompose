package com.example.mp3playerviacompose.data.databases.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mp3playerviacompose.data.databases.dao.FavoriteDao
import com.example.mp3playerviacompose.data.databases.dao.PlayListDao
import com.example.mp3playerviacompose.domain.models.MediaModel
import com.example.mp3playerviacompose.domain.models.PlayListEntity
import com.example.mp3playerviacompose.domain.models.SongsPlayList

@Database(entities = [MediaModel::class, PlayListEntity::class, SongsPlayList::class], version = 1, exportSchema = false)
abstract class MusicDatabase : RoomDatabase() {
    abstract fun favDao(): FavoriteDao
    abstract fun playListDao(): PlayListDao
}