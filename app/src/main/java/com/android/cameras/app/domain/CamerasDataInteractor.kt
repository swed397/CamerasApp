package com.android.cameras.app.domain

interface CamerasDataInteractor {

    suspend fun refreshData(): List<CameraModel>
    suspend fun getAllData(): List<CameraModel>
    suspend fun populateBdData()
    suspend fun updateFavoriteById(id: Long)
}