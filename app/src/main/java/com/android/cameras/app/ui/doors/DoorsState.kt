package com.android.cameras.app.ui.doors

sealed interface DoorsState {
    data object Loading : DoorsState
    data class Content(val data: List<DoorUiModel>) : DoorsState
    data class Error(val error: String) : DoorsState
    data object Refresh: DoorsState
}