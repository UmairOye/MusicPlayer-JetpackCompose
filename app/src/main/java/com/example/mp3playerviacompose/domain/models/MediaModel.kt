package com.example.mp3playerviacompose.domain.models


data class MediaModel (
    val mediaId: Int = 0,
    val name: String = "",
    val artist: String = "",
    val uri: String = "",
    val path: String = "",
    val duration: Long = 0L,
    val formattedDuration: String = "",
    val size: Long = 0L,
    val formattedSize: String = "",
    val thumbnail: String? = null
)