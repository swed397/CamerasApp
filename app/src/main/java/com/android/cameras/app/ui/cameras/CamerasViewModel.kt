package com.android.cameras.app.ui.cameras

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.cameras.app.data.DataRepoImpl
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class CamerasViewModel @AssistedInject constructor(
    private val dataRepo: DataRepoImpl
) : ViewModel() {

    private val _state = MutableLiveData<CamerasState>(CamerasState.Loading)
    val state: LiveData<CamerasState> = _state

    init {
        viewModelScope.launch {
            val dataDef = async(Dispatchers.IO) { dataRepo.findData() }
            _state.value = CamerasState.Content(dataDef.await())
        }
    }

    @AssistedFactory
    fun interface Factory {
        fun create(): CamerasViewModel
    }
}