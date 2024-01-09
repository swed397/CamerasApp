package com.android.cameras.app.data

import com.android.cameras.app.data.bd.CamerasBdRepoImpl
import com.android.cameras.app.data.network.CamerasNetworkRepoImpl
import com.android.cameras.app.domain.CameraModel
import javax.inject.Inject

class CamerasDataRepoImpl @Inject constructor(
    private val networkRepo: CamerasNetworkRepoImpl,
    private val bdRepo: CamerasBdRepoImpl
) {

    suspend fun refreshData(): List<CameraModel> {
        val bdData = bdRepo.findAll()

        return if (bdData.isEmpty()) {
            val newData = networkRepo.getAllCameras()
            bdRepo.saveAll(newData)
            newData
        } else {
            val apiData = networkRepo.getAllCameras()
            val bdIds = bdRepo.findAllIds()
            val newData = apiData.filter { bdIds.contains(it.id).not() }
            bdRepo.saveAll(newData)
            bdData + newData
        }
    }

    suspend fun getAllData(): List<CameraModel> =
        bdRepo.findAll().ifEmpty {
            val data = networkRepo.getAllCameras()
            bdRepo.saveAll(data)
            data
        }

    suspend fun updateFavoriteCameraById(id: Long) {
        bdRepo.updateFavoriteCameraById(id)
    }
}