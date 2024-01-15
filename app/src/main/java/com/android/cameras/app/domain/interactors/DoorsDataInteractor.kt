package com.android.cameras.app.domain.interactors

import com.android.cameras.app.domain.models.DoorModel

interface DoorsDataInteractor {

    suspend fun refreshData(): List<DoorModel>
    suspend fun getAllData(): List<DoorModel>
    suspend fun updateFavoriteById(id: Long)
    suspend fun updateNameDoorById(id: Long, name: String)
}