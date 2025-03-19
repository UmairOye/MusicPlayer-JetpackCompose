package com.example.mp3playerviacompose.domain.models

data class Artist(
    val id: Long,
    val artistName: String,
    val numberOfTracks: Int,
    val numberOfAlbums: Int
)
