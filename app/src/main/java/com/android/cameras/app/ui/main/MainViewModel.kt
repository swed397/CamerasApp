package com.android.cameras.app.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.cameras.app.domain.repo.DbRepo
import com.android.cameras.app.domain.repo.NetworkRepo
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel @AssistedInject constructor(
    private val dbRepo: DbRepo,
    private val networkRepo: NetworkRepo
) : ViewModel() {

    init {
        populateCameras()
        populateDoors()
    }

    private fun populateCameras() {
        viewModelScope.launch(Dispatchers.IO) {
            dbRepo.findAllCameras().ifEmpty {
                val data = networkRepo.getAllCameras()
                dbRepo.saveAllCameras(data)
            }
        }
    }

    private fun populateDoors() {
        viewModelScope.launch(Dispatchers.IO) {
            dbRepo.findAllDoors().ifEmpty {
                val data = networkRepo.getAllDoors()
                dbRepo.saveAllDoors(data)
            }
        }
    }

    @AssistedFactory
    fun interface Factory {
        fun create(): MainViewModel
    }
}