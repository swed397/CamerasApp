package com.android.cameras.app.data

import com.android.cameras.app.data.bd.DbRepoImpl
import com.android.cameras.app.data.network.NetworkRepoImpl
import com.android.cameras.app.domain.CameraModel
import com.android.cameras.app.domain.DoorModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class DoorsDataRepoImpl @Inject constructor(
    private val networkRepo: NetworkRepoImpl,
    private val bdRepo: DbRepoImpl
) {

    suspend fun refreshData(): List<DoorModel> {
        val bdData = bdRepo.findAllDoors()

        return if (bdData.isEmpty()) {
            val newData = networkRepo.getAllDoors()
            coroutineScope {
                launch {
                    bdRepo.saveAllDoors(newData)
                }
            }
            newData
        } else {
            val apiData = networkRepo.getAllDoors()
            val bdIds = bdRepo.findAllDoorsIds()
            val newData = apiData.filter { bdIds.contains(it.id).not() }
            coroutineScope {
                launch {
                    bdRepo.saveAllDoors(newData)
                }
            }
            bdData + newData
        }
    }

    suspend fun getAllData(): List<DoorModel> = bdRepo.findAllDoors()

    suspend fun populateBdData() {
        bdRepo.findAllDoors().ifEmpty {
            val data = networkRepo.getAllDoors()
            bdRepo.saveAllDoors(data)
        }
    }

    suspend fun updateFavoriteDoorById(id: Long) {
        bdRepo.updateFavoriteDoorById(id)
    }

    suspend fun updateNameDoorById(id: Long, name: String) {
        bdRepo.updateNameDoorById(id, name)
    }
}