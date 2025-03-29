package com.example.mp3playerviacompose.domain.repositories

import com.example.mp3playerviacompose.domain.models.MediaModel
import kotlinx.coroutines.flow.Flow

interface FavoriteRepository {
    suspend fun insertFavorite(mediaModel: MediaModel)
    suspend fun getAllFavorites(): Flow<List<MediaModel>>
    suspend fun isFavorite(path: String): Boolean
    suspend fun unFavorite(mediaModel: MediaModel)
    suspend fun updateFavorite(path: String, isFavorite: Boolean)

}