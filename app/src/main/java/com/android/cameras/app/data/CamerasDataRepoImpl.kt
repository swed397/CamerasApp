package com.android.cameras.app.data

import com.android.cameras.app.data.bd.DbRepoImpl
import com.android.cameras.app.data.network.NetworkRepoImpl
import com.android.cameras.app.domain.CameraModel
import javax.inject.Inject

class CamerasDataRepoImpl @Inject constructor(
    private val networkRepo: NetworkRepoImpl,
    private val bdRepo: DbRepoImpl
) {

    suspend fun refreshData(): List<CameraModel> {
        val bdData = bdRepo.findAllCameras()

        return if (bdData.isEmpty()) {
            val newData = networkRepo.getAllCameras()
            bdRepo.saveAllCameras(newData)
            newData
        } else {
            val apiData = networkRepo.getAllCameras()
            val bdIds = bdRepo.findAllCamerasIds()
            val newData = apiData.filter { bdIds.contains(it.id).not() }
            bdRepo.saveAllCameras(newData)
            bdData + newData
        }
    }

    suspend fun getAllData(): List<CameraModel> = bdRepo.findAllCameras()

    suspend fun populateBdData() {
        bdRepo.findAllCameras().ifEmpty {
            val data = networkRepo.getAllCameras()
            bdRepo.saveAllCameras(data)
        }
    }

    suspend fun updateFavoriteCameraById(id: Long) {
        bdRepo.updateFavoriteCameraById(id)
    }
}