package com.example.mp3playerviacompose.data.databases.favoriteRepository

import com.example.mp3playerviacompose.data.databases.dao.FavoriteDao
import com.example.mp3playerviacompose.domain.models.MediaModel
import com.example.mp3playerviacompose.domain.repositories.FavoriteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FavoriteRepositoryImplementation @Inject constructor(private val favoriteDao: FavoriteDao) :
    FavoriteRepository {

    override suspend fun insertFavorite(mediaModel: MediaModel) = withContext(Dispatchers.IO) {
        if(isFavorite(mediaModel.path)){
            unFavorite(mediaModel = mediaModel)
        }else{
            favoriteDao.insertFavorite(mediaModel)
        }
    }

    override suspend fun getAllFavorites(): Flow<List<MediaModel>> {
        return favoriteDao.getAllFavorites()
    }

    override suspend fun isFavorite(path: String): Boolean = withContext(Dispatchers.IO){
        return@withContext favoriteDao.doesPathExist(path)
    }

    override suspend fun unFavorite(mediaModel: MediaModel) = withContext(Dispatchers.IO) {
        favoriteDao.deletePath(mediaModel)
    }

    override suspend fun updateFavorite(path: String, isFavorite: Boolean) = withContext(Dispatchers.IO){
        favoriteDao.updateFavorites(path, isFavorite)
    }

}