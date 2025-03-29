package com.example.mp3playerviacompose.domain.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tbl_songsPlaylist")
data class SongsPlayList(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val name: String = "",
    val artist: String = "",
    val uri: String = "",
    val path: String = "",
    val duration: Long = 0L,
    val formattedDuration: String = "",
    val size: Long = 0L,
    val formattedSize: String = "",
    val thumbnail: String? = null,
    var isFavorite: Boolean = false,
    var playListName:String? = null
)
