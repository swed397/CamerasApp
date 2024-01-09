package com.android.cameras.app.ui.cameras

sealed interface CamerasState {
    data object Loading : CamerasState
    data class Content(val data: Map<String, List<CameraUiModel>>) : CamerasState
    data class Error(val error: String): CamerasState
}