package com.android.cameras.app.ui.cameras

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.cameras.app.R
import com.android.cameras.app.data.CamerasDataRepoImpl
import com.android.cameras.app.ui.doors.DoorsState
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class CamerasViewModel @AssistedInject constructor(
    private val dataRepo: CamerasDataRepoImpl,
    private val camerasUiModelMapper: CamerasUiModelMapper
) : ViewModel() {

    private val _state = MutableLiveData<CamerasState>(CamerasState.Loading)
    val state: LiveData<CamerasState> = _state

    init {
        viewModelScope.launch {
            async(Dispatchers.IO) {
                val data = dataRepo.getAllData()
                camerasUiModelMapper(data)
            }.apply {
                try {
                    _state.value = CamerasState.Content(this.await())
                } catch (e: RuntimeException) {
                    _state.value = CamerasState.Error(e.message ?: "Something goes wrong")
                }
            }
        }
    }

    fun updateFavoriteCameraById(id: Long, key: String) {
        viewModelScope.launch {
            launch(Dispatchers.IO) {
                dataRepo.updateFavoriteCameraById(id)
            }
            when (val currentState = _state.value) {
                is CamerasState.Content -> {
                    val mutableItems = currentState.data[key]!!.toMutableList()
                    val index = mutableItems.indexOfFirst { it.id == id }
                    val item = mutableItems[index]
                    mutableItems[index] = item.copy(
                        favorites = item.favorites.not(),
                        favoritesButtonIcon = if (item.favorites) R.drawable.baseline_star_border_20 else R.drawable.baseline_star_full_20
                    )

                    val newData = currentState.data.toMutableMap()
                    newData[key] = mutableItems
                    _state.value = CamerasState.Content(data = newData)
                }

                is CamerasState.Loading -> {}
                is CamerasState.Error -> {}
                else -> {}
            }
        }
    }

    fun refreshData() {
        _state.value = CamerasState.Refresh
        viewModelScope.launch {
            async(Dispatchers.IO) {
                dataRepo.refreshData()
            }.apply {
                try {
                    _state.value = CamerasState.Content(camerasUiModelMapper(this.await()))
                } catch (e: RuntimeException) {
                    _state.value = CamerasState.Error(e.message ?: "Something goes wrong")
                }
            }
        }
    }

    @AssistedFactory
    fun interface Factory {
        fun create(): CamerasViewModel
    }
}