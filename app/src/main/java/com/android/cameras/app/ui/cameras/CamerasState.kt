package com.android.cameras.app.ui.cameras

import com.android.cameras.app.domain.CameraModel

sealed interface CamerasState {
    data object Loading : CamerasState
    data class Content(val data: Map<String, List<CameraModel>>) : CamerasState
}