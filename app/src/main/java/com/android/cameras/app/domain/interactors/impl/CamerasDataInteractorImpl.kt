package com.android.cameras.app.domain.interactors.impl

import com.android.cameras.app.data.bd.DbRepoImpl
import com.android.cameras.app.data.network.NetworkRepoImpl
import com.android.cameras.app.domain.models.CameraModel
import com.android.cameras.app.domain.interactors.CamerasDataInteractor
import com.android.cameras.app.domain.repo.DbRepo
import com.android.cameras.app.domain.repo.NetworkRepo
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class CamerasDataInteractorImpl @Inject constructor(
    private val networkRepo: NetworkRepo,
    private val bdRepo: DbRepo
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

    override suspend fun updateFavoriteById(id: Long) {
        bdRepo.updateFavoriteCameraById(id)
    }
}