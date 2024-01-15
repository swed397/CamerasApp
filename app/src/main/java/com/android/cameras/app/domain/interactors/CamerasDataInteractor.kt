package com.android.cameras.app.domain.interactors

import com.android.cameras.app.domain.models.CameraModel

interface CamerasDataInteractor {

    suspend fun refreshData(): List<CameraModel>
    suspend fun getAllData(): List<CameraModel>
    suspend fun updateFavoriteById(id: Long)
}