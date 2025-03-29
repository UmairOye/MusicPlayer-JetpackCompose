package com.example.mp3playerviacompose.data.device.repositories

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.example.mp3playerviacompose.domain.models.Album
import com.example.mp3playerviacompose.domain.models.Artist
import com.example.mp3playerviacompose.domain.models.AudioFolder
import com.example.mp3playerviacompose.domain.models.MediaModel
import com.example.mp3playerviacompose.domain.models.RequestState
import com.example.mp3playerviacompose.domain.repositories.FavoriteRepository
import com.example.mp3playerviacompose.domain.repositories.MediaFetchingRepository
import com.example.mp3playerviacompose.domain.utils.Constants
import com.example.mp3playerviacompose.domain.utils.Utils
import com.example.mp3playerviacompose.domain.utils.formatDuration
import com.example.mp3playerviacompose.domain.utils.getFileSize
import com.example.mp3playerviacompose.domain.utils.hasPermission
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.File
import javax.inject.Inject


class MediaFetchRepositoryImplementation @Inject constructor(@ApplicationContext private val context: Context,
    private val favoriteRepository: FavoriteRepository) :
    MediaFetchingRepository {





    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun fetchAudioFiles(): Flow<RequestState<SnapshotStateList<MediaModel>>> = flow {
        val deviceAudios = mutableStateListOf<MediaModel>()
        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.DISPLAY_NAME,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.SIZE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ALBUM_ID
        )

        val sortOrder = "${MediaStore.Audio.Media.DATE_ADDED} DESC"

        val cursor = context.contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            sortOrder
        )

        cursor?.use {
            val idColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
            val nameColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)
            val dataColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
            val durationColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
            val sizeColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE)
            val artistColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
            val albumIdColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)

            while (it.moveToNext()) {
                val mediaId = it.getInt(idColumn)
                val name = it.getString(nameColumn)
                val path = it.getString(dataColumn)
                val duration = it.getLong(durationColumn)
                val size = it.getLong(sizeColumn)
                val artist = it.getString(artistColumn) ?: "Unknown Artist"
                val uri = ContentUris.withAppendedId(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    it.getLong(idColumn)
                ).toString()
                val albumId = it.getLong(albumIdColumn)
                val albumArtUri = getAlbumArtUri(albumId)


                deviceAudios.add(
                    MediaModel(
                        mediaId,
                        name, artist, uri, path, duration, duration.formatDuration(),
                        size, formattedSize = size.getFileSize(), thumbnail = albumArtUri,
                        isFavorite = favoriteRepository.isFavorite(path)
                    )
                )
            }
        }


        emit(RequestState.Success(deviceAudios))
    }


    override fun getAlbumArtUri(albumId: Long?): String? {
        return albumId?.let {
            "content://media/external/audio/albumart/$it"
        }
    }


    override fun getLoadingList(): List<MediaModel> {
        val emptyList = mutableListOf(
            MediaModel(0),
            MediaModel(1),
            MediaModel(2),
            MediaModel(3),
            MediaModel(4),
            MediaModel(5)
        )
        return emptyList
    }


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override suspend fun getArtists(): Flow<RequestState<List<Artist>>> = flow {
        emit(RequestState.Loading)
        try {
                val artistList = mutableListOf<Artist>()
                val projection = arrayOf(
                    MediaStore.Audio.Artists._ID,
                    MediaStore.Audio.Artists.ARTIST,
                    MediaStore.Audio.Artists.NUMBER_OF_TRACKS,
                    MediaStore.Audio.Artists.NUMBER_OF_ALBUMS
                )

                val cursor: Cursor? = context.contentResolver.query(
                    MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI,
                    projection,
                    null,
                    null,
                    MediaStore.Audio.Artists.ARTIST + " ASC"
                )

                cursor?.use {
                    if (it.moveToFirst() ) {
                        do {
                            val id = it.getLong(it.getColumnIndexOrThrow(MediaStore.Audio.Artists._ID))
                            val artistName =
                                it.getString(it.getColumnIndexOrThrow(MediaStore.Audio.Artists.ARTIST))
                            val numberOfTracks =
                                it.getInt(it.getColumnIndexOrThrow(MediaStore.Audio.Artists.NUMBER_OF_TRACKS))
                            val numberOfAlbums =
                                it.getInt(it.getColumnIndexOrThrow(MediaStore.Audio.Artists.NUMBER_OF_ALBUMS))

                            val artist = Artist(id, artistName, numberOfTracks, numberOfAlbums)
                            artistList.add(artist)
                        } while (it.moveToNext())
                    }
                }

                emit(RequestState.Success(artistList))

            } catch (e: SecurityException) {
                emit(RequestState.Error(e.cause!!))
            } catch (e: Exception) {
                emit(RequestState.Error(e.cause!!))
            }
    }.flowOn(Dispatchers.IO)


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun getAlbums(): Flow<RequestState<List<Album>>> = flow {
        emit(RequestState.Loading)

        if (context.hasPermission(Constants.permission)) {
            try {
                val albumList = mutableListOf<Album>()
                val projection = arrayOf(
                    MediaStore.Audio.Albums._ID,
                    MediaStore.Audio.Albums.ALBUM,
                    MediaStore.Audio.Albums.ARTIST,
                    MediaStore.Audio.Albums.NUMBER_OF_SONGS
                )

                val cursor: Cursor? = context.contentResolver.query(
                    MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                    projection,
                    null,
                    null,
                    MediaStore.Audio.Albums.ALBUM + " ASC"
                )

                cursor?.use {
                    if (it.moveToFirst()) {
                        do {
                            val id = it.getLong(it.getColumnIndexOrThrow(MediaStore.Audio.Albums._ID))
                            val albumName =
                                it.getString(it.getColumnIndexOrThrow(MediaStore.Audio.Albums.ALBUM))
                            val artistName =
                                it.getString(it.getColumnIndexOrThrow(MediaStore.Audio.Albums.ARTIST))
                            val numberOfSongs =
                                it.getInt(it.getColumnIndexOrThrow(MediaStore.Audio.Albums.NUMBER_OF_SONGS))

                            val album = Album(id, albumName, artistName, numberOfSongs)
                            albumList.add(album)
                        } while (it.moveToNext())
                    }
                }

                emit(RequestState.Success(albumList))

            } catch (exception: SecurityException) {
                emit(RequestState.Error(exception))
            } catch (e: Exception) {
                emit(RequestState.Error(e))
            }
        }

    }.flowOn(Dispatchers.IO)


    override fun getAudioFolders(): Flow<RequestState<List<AudioFolder>>> = flow {
        emit(RequestState.Loading)

        try {
            val audioFolderList = mutableListOf<AudioFolder>()
            val uri: Uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
            val projection = arrayOf(
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DISPLAY_NAME
            )
            val selection: String? = MediaStore.Audio.Media.IS_MUSIC + " != 0"
            val sortOrder: String? = null

            val cursor: Cursor? =
                context.contentResolver.query(uri, projection, selection, null, sortOrder)

            cursor?.use {
                if (it.moveToFirst()) {
                    val pathColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
                    val displayNameColumn =
                        it.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)

                    do {
                        val filePath = it.getString(pathColumn)
                        val fileName = it.getString(displayNameColumn)
                        val file = File(filePath)
                        val folderPath = file.parent ?: continue
                        val existingFolder =
                            audioFolderList.find { folder -> folder.path == folderPath }

                        if (existingFolder != null) {
                            audioFolderList[audioFolderList.indexOf(existingFolder)] =
                                existingFolder.copy(numberOfSongs = existingFolder.numberOfSongs + 1)
                        } else {
                            val folderName = File(folderPath).name
                            audioFolderList.add(AudioFolder(folderPath, folderName, 1))
                        }

                    } while (it.moveToNext())
                }
            }

            emit(RequestState.Success(audioFolderList))

        } catch (securityException: SecurityException) {
            emit(RequestState.Error(securityException))
        } catch (e: Exception) {
            emit(RequestState.Error(e))
        }
    }.flowOn(Dispatchers.IO)


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun fetchAudioFilesByArtist(artistName: String): Flow<RequestState<List<MediaModel>>> =
        flow {
            emit(RequestState.Loading)
            if (context.hasPermission(Constants.permission)) {
                try {
                    val deviceAudios = mutableListOf<MediaModel>()
                    val projection = arrayOf(
                        MediaStore.Audio.Media._ID,
                        MediaStore.Audio.Media.DISPLAY_NAME,
                        MediaStore.Audio.Media.DATA,
                        MediaStore.Audio.Media.DURATION,
                        MediaStore.Audio.Media.SIZE,
                        MediaStore.Audio.Media.ARTIST,
                        MediaStore.Audio.Media.ALBUM_ID
                    )

                    val selection = "${MediaStore.Audio.Media.ARTIST} = ?"
                    val selectionArgs = arrayOf(artistName)
                    val sortOrder = "${MediaStore.Audio.Media.TITLE} ASC"

                    val cursor = context.contentResolver.query(
                        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                        projection,
                        selection,
                        selectionArgs,
                        sortOrder
                    )

                    cursor?.use {
                        val idColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
                        val nameColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)
                        val dataColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
                        val durationColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
                        val sizeColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE)
                        val artistColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
                        val albumIdColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)

                        while (it.moveToNext()) {
                            val mediaId = it.getInt(idColumn)
                            val name = it.getString(nameColumn)
                            val path = it.getString(dataColumn)
                            val duration = it.getLong(durationColumn)
                            val size = it.getLong(sizeColumn)
                            val artist = it.getString(artistColumn) ?: "Unknown Artist" // handle null artist
                            val uri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, it.getLong(idColumn)
                            ).toString()
                            val albumId = it.getLong(albumIdColumn)
                            val albumArtUri = getAlbumArtUri(albumId)

                            deviceAudios.add(
                                MediaModel(
                                    mediaId,
                                    name, artist, uri, path, duration, duration.formatDuration(),
                                    size, formattedSize = size.getFileSize(), thumbnail = albumArtUri
                                )
                            )
                        }
                    }
                    emit(RequestState.Success(deviceAudios))

                } catch (e: Exception) {
                    Log.e("MediaStoreHelper", "Error fetching audio files by artist: ${e.message}", e)
                    emit(RequestState.Error(e))
                }
            }

        }.flowOn(Dispatchers.IO)




    override fun fetchAudioFilesByAlbums(albumName: String): Flow<RequestState<List<MediaModel>>> =
        flow<RequestState<List<MediaModel>>> {
            emit(RequestState.Loading)
            val audioList = mutableListOf<MediaModel>()
            val uri: Uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
            val selection: String = MediaStore.Audio.Media.ALBUM + " = ?"
            val selectionArgs = arrayOf(albumName)
            val projection = arrayOf(
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.SIZE,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.ALBUM_ID
            )

            val sortOrder = MediaStore.Audio.Media.DISPLAY_NAME + " ASC"
            var cursor: Cursor? = null
            try {
                cursor = context.contentResolver.query(
                    uri,
                    projection,
                    selection,
                    selectionArgs,
                    sortOrder
                )

                if (cursor != null && cursor.moveToFirst()) {
                    do {
                        val mediaId = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID))
                        val name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME))
                        val artist = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST))
                        val path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA))
                        val duration = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION))
                        val size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE))
                        val album = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM))
                        val contentUri: Uri = ContentUris.withAppendedId(
                            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                            mediaId.toLong()
                        )
                        val albumIdColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)

                        val albumId = cursor.getLong(albumIdColumn)
                        val albumArtUri = getAlbumArtUri(albumId)

                        val formattedDuration = duration.formatDuration()
                        val formattedSize = size.getFileSize()
                        val mediaModel = MediaModel(
                            mediaId = mediaId,
                            name = name,
                            artist = artist,
                            uri = contentUri.toString(),
                            path = path,
                            duration = duration,
                            formattedDuration = formattedDuration,
                            size = size,
                            formattedSize = formattedSize,
                            thumbnail = getAlbumArtUri(albumId)
                        )

                        audioList.add(mediaModel)

                    } while (cursor.moveToNext())
                } else {
                    Utils.printLog(message = "No audio files found for album: $albumName")
                }

            } catch (e: Exception) {
                Utils.printLog(message = "Error fetching audio files by album: ${e.message}")
            } finally {
                cursor?.close()
            }

            emit(RequestState.Success(audioList))


        }.flowOn(Dispatchers.IO)

    override fun fetchAudioFilesFromFolder(folderName: String): Flow<RequestState<List<MediaModel>>> = flow<RequestState<List<MediaModel>>> {
        val audioList = mutableListOf<MediaModel>()
        val uri: Uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val selection: String = MediaStore.Audio.Media.DATA + " LIKE ?"
        val selectionArgs = arrayOf("%/$folderName/%")
        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.DISPLAY_NAME,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.SIZE,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.ALBUM_ID
        )

        val sortOrder = MediaStore.Audio.Media.DISPLAY_NAME + " ASC"
        var cursor: Cursor? = null

        try {
            cursor = context.contentResolver.query(
                uri,
                projection,
                selection,
                selectionArgs,
                sortOrder
            )

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    val mediaId = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID))
                    val name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME))
                    val artist = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST))
                    val path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA))
                    val duration = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION))
                    val size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE))
                    val album = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM))
                    val contentUri: Uri = ContentUris.withAppendedId(
                        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                        mediaId.toLong()
                    )
                    val albumIdColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)
                    val albumId = cursor.getLong(albumIdColumn)
                    val albumArtUri = getAlbumArtUri(albumId)


                    val formattedDuration = duration.formatDuration()
                    val formattedSize = size.getFileSize()

                    val mediaModel = MediaModel(
                        mediaId = mediaId,
                        name = name,
                        artist = artist,
                        uri = contentUri.toString(),
                        path = path,
                        duration = duration,
                        formattedDuration = formattedDuration,
                        size = size,
                        formattedSize = formattedSize,
                        thumbnail = albumArtUri
                    )

                    audioList.add(mediaModel)
                } while (cursor.moveToNext())
            } else {
                Utils.printLog("No audio files found in folder: $folderName")
            }
        } catch (e: Exception) {
            emit(RequestState.Error(e))
            Utils.printLog(message = "Error fetching audio files from folder: ${e.message}")
        } finally {
            cursor?.close()
        }
        emit(RequestState.Success(audioList))

    }.flowOn(Dispatchers.IO)

}