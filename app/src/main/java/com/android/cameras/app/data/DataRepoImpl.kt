package com.android.cameras.app.data

import com.android.cameras.app.data.bd.BdRepoImpl
import com.android.cameras.app.data.network.NetworkRepoImpl
import com.android.cameras.app.domain.CameraModel
import javax.inject.Inject

class DataRepoImpl @Inject constructor(
    private val networkRepo: NetworkRepoImpl,
    private val bdRepo: BdRepoImpl
) {

    suspend fun findData(): Map<String, List<CameraModel>> {
        val bdData = bdRepo.findAll()

        return if (bdData.isEmpty()) {
            val newData = networkRepo.getAllCameras()
            bdRepo.saveAll(newData)
            newData.groupBy { it.category ?: "Other" }
        } else {
            val apiData = networkRepo.getAllCameras()
            val bdIds = bdRepo.findAllIds()
            val newData = apiData.filter { bdIds.contains(it.id).not() }
            bdRepo.saveAll(newData)
            (bdData + newData).groupBy { it.category ?: "Other" }
        }
    }
}