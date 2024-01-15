package com.android.cameras.app.domain.repo

import com.android.cameras.app.domain.models.CameraModel
import com.android.cameras.app.domain.models.DoorModel

interface DbRepo {
    suspend fun findAllCameras(): List<CameraModel>
    suspend fun findAllCamerasIds(): List<Long>
    suspend fun saveAllCameras(data: List<CameraModel>)
    suspend fun updateFavoriteCameraById(id: Long)
    suspend fun findAllDoors(): List<DoorModel>
    suspend fun saveAllDoors(data: List<DoorModel>)
    suspend fun updateFavoriteDoorById(id: Long)
    suspend fun updateNameDoorById(id: Long, name: String)
    suspend fun findAllDoorsIds(): List<Long>
}