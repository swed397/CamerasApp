package com.android.cameras.app.ui.cameras

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.cameras.app.R
import com.android.cameras.app.domain.interactors.CamerasDataInteractor
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

class CamerasViewModel @AssistedInject constructor(
    private val dataInteractor: CamerasDataInteractor,
    private val camerasUiModelMapper: CamerasUiModelMapper
) : ViewModel() {

    private val _state = MutableLiveData<CamerasState>(CamerasState.Loading)
    val state: LiveData<CamerasState> = _state

    init {
        _state.value = CamerasState.Loading
        viewModelScope.launch {
            val result = async(Dispatchers.IO) {
                dataInteractor.getAllData()
            }

            try {
                _state.value = CamerasState.Content(
                    data = camerasUiModelMapper(result.await()),
                    isRefreshing = false
                )
            } catch (e: CancellationException) {
                throw e
            } catch (e: RuntimeException) {
                _state.value = CamerasState.Error(e.message ?: "Something goes wrong")
            }
        }
    }

    fun updateFavoriteCameraById(id: Long, key: String) {
        viewModelScope.launch {
            launch(Dispatchers.IO) {
                dataInteractor.updateFavoriteById(id)
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
                    _state.value = CamerasState.Content(data = newData, isRefreshing = false)
                }

                is CamerasState.Loading -> {}
                is CamerasState.Error -> {}
                else -> {}
            }
        }
    }

    fun refreshData() {
        _state.value = when (val currentState = _state.value) {
            is CamerasState.Content -> currentState.copy(isRefreshing = true)
            else -> currentState
        }

        viewModelScope.launch {
            val result = async(Dispatchers.IO) {
                dataInteractor.refreshData()
            }

            try {
                _state.value = CamerasState.Content(
                    data = camerasUiModelMapper(result.await()),
                    isRefreshing = false
                )
            } catch (e: CancellationException) {
                throw e
            } catch (e: RuntimeException) {
                _state.value = CamerasState.Error(e.message ?: "Something goes wrong")
            }
        }
    }

    @AssistedFactory
    fun interface Factory {
        fun create(): CamerasViewModel
    }
}