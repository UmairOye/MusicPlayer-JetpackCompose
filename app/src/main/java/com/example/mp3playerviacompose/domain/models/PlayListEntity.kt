package com.example.mp3playerviacompose.domain.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tbl_playList")
data class PlayListEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val playListName: String? = null,
    val songDuration: Long? = 0L,
    val songsCount: Int = 0,
    var isChecked: Boolean = false
)