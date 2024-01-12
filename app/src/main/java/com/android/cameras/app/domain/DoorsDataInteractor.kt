package com.android.cameras.app.domain

interface DoorsDataInteractor {

    suspend fun refreshData(): List<DoorModel>
    suspend fun getAllData(): List<DoorModel>
    suspend fun populateBdData()
    suspend fun updateFavoriteById(id: Long)
    suspend fun updateNameDoorById(id: Long, name: String)
}