package com.android.cameras.app.data

import com.android.cameras.app.data.bd.DbRepoImpl
import com.android.cameras.app.data.network.NetworkRepoImpl
import com.android.cameras.app.domain.CameraModel
import com.android.cameras.app.domain.CamerasDataInteractor
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class CamerasDataInteractorImpl @Inject constructor(
    private val networkRepo: NetworkRepoImpl,
    private val bdRepo: DbRepoImpl
) : CamerasDataInteractor {

    override suspend fun refreshData(): List<CameraModel> {
        val bdData = bdRepo.findAllCameras()

        return if (bdData.isEmpty()) {
            val newData = networkRepo.getAllCameras()
            coroutineScope {
                launch {
                    bdRepo.saveAllCameras(newData)
                }
            }
            newData
        } else {
            val apiData = networkRepo.getAllCameras()
            val bdIds = bdRepo.findAllCamerasIds()
            val newData = apiData.filter { bdIds.contains(it.id).not() }
            coroutineScope {
                launch {
                    bdRepo.saveAllCameras(newData)
                }
            }
            bdData + newData
        }
    }

    override suspend fun getAllData(): List<CameraModel> = bdRepo.findAllCameras()

    override suspend fun populateBdData() {
        bdRepo.findAllCameras().ifEmpty {
            val data = networkRepo.getAllCameras()
            bdRepo.saveAllCameras(data)
        }
    }

    override suspend fun updateFavoriteById(id: Long) {
        bdRepo.updateFavoriteCameraById(id)
    }
}