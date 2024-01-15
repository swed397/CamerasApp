package com.android.cameras.app.ui.cameras

sealed interface CamerasState {
    val isRefreshing: Boolean
        get() = false
    data object Loading : CamerasState
    data class Content(
        val data: Map<String, List<CameraUiModel>>,
        override val isRefreshing: Boolean
    ) : CamerasState

    data class Error(val error: String) : CamerasState

}