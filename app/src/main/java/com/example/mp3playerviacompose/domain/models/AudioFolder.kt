package com.example.mp3playerviacompose.domain.models

import androidx.compose.runtime.Immutable

@Immutable
data class AudioFolder(val path: String, val displayName: String, val numberOfSongs: Int)
