package com.android.cameras.app.ui.doors

sealed interface DoorsState {
    val isRefreshing: Boolean
        get() = false

    data object Loading : DoorsState
    data class Content(val data: List<DoorUiModel>, override val isRefreshing: Boolean) : DoorsState
    data class Error(val error: String) : DoorsState
}