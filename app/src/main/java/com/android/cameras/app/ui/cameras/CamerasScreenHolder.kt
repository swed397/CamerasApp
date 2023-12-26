package com.android.cameras.app.ui.cameras

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import com.android.cameras.app.App
import com.android.cameras.app.di.injectedViewModel
import com.android.cameras.app.domain.CameraModel

@Composable
fun CamerasScreenHolder() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Yellow)
    ) {
        val context = LocalContext.current.applicationContext
        val viewModel = injectedViewModel {
            (context as App).appComponent.camerasViewModelFactory.create()
        }
        val state by viewModel.state.observeAsState()

        when (val currentState = state) {
            is CamerasState.Content -> CamerasList(data = currentState.data)
            is CamerasState.Loading -> {}
            else -> {}
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun CamerasList(data: Map<String, List<CameraModel>>) {
    LazyColumn {
        data.forEach { (title, data) ->
            stickyHeader {
                Text(text = title)
            }

            items(data, key = { it.id }) {
                CamerasListItem(cameraModel = it)
            }
        }
    }
}

@Composable
private fun CamerasListItem(cameraModel: CameraModel) {
    AsyncImage(model = cameraModel.imageUrl, contentDescription = null)
}