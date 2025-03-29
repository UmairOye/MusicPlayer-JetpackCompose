package com.example.mp3playerviacompose.domain.repositories

import androidx.compose.runtime.snapshots.SnapshotStateList
import com.example.mp3playerviacompose.domain.models.Album
import com.example.mp3playerviacompose.domain.models.Artist
import com.example.mp3playerviacompose.domain.models.AudioFolder
import com.example.mp3playerviacompose.domain.models.MediaModel
import com.example.mp3playerviacompose.domain.models.RequestState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


interface MediaFetchingRepository {
    fun fetchAudioFiles(): Flow<RequestState<SnapshotStateList<MediaModel>>>
    fun getAlbumArtUri(albumId: Long?): String?
    fun getLoadingList(): List<MediaModel>
    suspend fun getArtists(): Flow<RequestState<List<Artist>>>
    fun getAlbums(): Flow<RequestState<List<Album>>>
    fun getAudioFolders(): Flow<RequestState<List<AudioFolder>>>
    fun fetchAudioFilesByArtist(artistName: String): Flow<RequestState<List<MediaModel>>>
    fun fetchAudioFilesByAlbums(albumName: String): Flow<RequestState<List<MediaModel>>>
    fun fetchAudioFilesFromFolder(folderName: String): Flow<RequestState<List<MediaModel>>>
}