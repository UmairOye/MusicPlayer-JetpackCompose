package com.example.mp3playerviacompose.domain.models

data class Album(
    val id: Long,
    val albumName: String,
    val artistName: String,
    val numberOfSongs: Int,
)
