package com.android.cameras.app.domain.repo

import com.android.cameras.app.domain.models.CameraModel
import com.android.cameras.app.domain.models.DoorModel

interface NetworkRepo {
    suspend fun getAllCameras(): List<CameraModel>
    suspend fun getAllDoors(): List<DoorModel>
}