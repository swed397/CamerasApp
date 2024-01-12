package com.android.cameras.app.ui.doors

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.cameras.app.R
import com.android.cameras.app.domain.DoorsDataInteractor
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class DoorsViewModel @AssistedInject constructor(
    private val dataInteractor: DoorsDataInteractor,
    private val doorsUiModelMapper: DoorsUiModelMapper
) : ViewModel() {

    private val _state = MutableLiveData<DoorsState>(DoorsState.Loading)
    val state: LiveData<DoorsState> = _state

    init {
        _state.value = DoorsState.Loading
        viewModelScope.launch {
            async(Dispatchers.IO) {
                dataInteractor.getAllData()
            }.apply {
                try {
                    _state.value = DoorsState.Content(doorsUiModelMapper(this.await()))
                } catch (e: RuntimeException) {
                    _state.value = DoorsState.Error(e.message ?: "Something goes wrong")
                }
            }
        }
    }

    fun updateFavoriteDoorById(id: Long) {
        viewModelScope.launch {
            launch(Dispatchers.IO) {
                dataInteractor.updateFavoriteById(id)
            }
            when (val currentState = _state.value) {
                is DoorsState.Content -> {
                    val mutableItems = currentState.data.toMutableList()
                    val index = mutableItems.indexOfFirst { it.id == id }
                    val item = mutableItems[index]
                    mutableItems[index] = item.copy(
                        favorites = item.favorites.not(),
                        favoritesButtonIcon = if (item.favorites) R.drawable.baseline_star_border_20 else R.drawable.baseline_star_full_20
                    )

                    _state.value = DoorsState.Content(data = mutableItems)
                }

                is DoorsState.Loading -> {}
                is DoorsState.Error -> {}
                else -> {}
            }
        }
    }

    fun updateNameDoorById(id: Long, name: String) {
        viewModelScope.launch {
            launch(Dispatchers.IO) {
                dataInteractor.updateNameDoorById(id, name)
            }
        }
        when (val currentState = _state.value) {
            is DoorsState.Content -> {
                val mutableItems = currentState.data.toMutableList()
                val index = mutableItems.indexOfFirst { it.id == id }
                val item = mutableItems[index]
                mutableItems[index] = item.copy(name = name)

                _state.value = DoorsState.Content(data = mutableItems)
            }

            is DoorsState.Loading -> {}
            is DoorsState.Error -> {}
            else -> {}
        }
    }

    fun refreshData() {
        _state.value = DoorsState.Refresh
        viewModelScope.launch {
            async(Dispatchers.IO) {
                dataInteractor.refreshData()
            }.apply {
                try {
                    _state.value = DoorsState.Content(doorsUiModelMapper(this.await()))
                } catch (e: RuntimeException) {
                    _state.value = DoorsState.Error(e.message ?: "Something goes wrong")
                }
            }
        }
    }

    @AssistedFactory
    fun interface Factory {
        fun create(): DoorsViewModel
    }
}