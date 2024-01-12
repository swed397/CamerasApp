package com.android.cameras.app.data

import com.android.cameras.app.data.bd.DbRepoImpl
import com.android.cameras.app.data.network.NetworkRepoImpl
import com.android.cameras.app.domain.DoorModel
import com.android.cameras.app.domain.DoorsDataInteractor
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class DoorsDataInteractorImpl @Inject constructor(
    private val networkRepo: NetworkRepoImpl,
    private val bdRepo: DbRepoImpl
) : DoorsDataInteractor {

    override suspend fun refreshData(): List<DoorModel> {
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

    override suspend fun getAllData(): List<DoorModel> = bdRepo.findAllDoors()

    override suspend fun populateBdData() {
        bdRepo.findAllDoors().ifEmpty {
            val data = networkRepo.getAllDoors()
            bdRepo.saveAllDoors(data)
        }
    }

    override suspend fun updateFavoriteById(id: Long) {
        bdRepo.updateFavoriteDoorById(id)
    }

    override suspend fun updateNameDoorById(id: Long, name: String) {
        bdRepo.updateNameDoorById(id, name)
    }
}