package com.android.cameras.app.ui.main

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.cameras.app.R
import com.android.cameras.app.ui.CameraType
import com.android.cameras.app.ui.cameras.CamerasScreenHolder
import com.android.cameras.app.ui.doors.DoorsScreenHolder
import com.android.cameras.app.ui.theme.CamerasAppTheme

@Composable
fun MainScreenHolder() {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        TopBarTitle()
        CamerasTab()
    }
}

@Composable
private fun TopBarTitle() {
    Text(
        text = "Мой дом",
        fontSize = 21.sp,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .padding(top = 60.dp)
            .fillMaxWidth()
    )
}

@Composable
private fun CamerasTab() {
    var cameraTypeState by remember { mutableStateOf(CameraType.CAMERAS) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 46.dp)
    ) {
    }
    TabRow(
        selectedTabIndex = when (cameraTypeState) {
            CameraType.CAMERAS -> 0
            CameraType.DOORS -> 1
        }
    ) {
        Tab(text = { Text(stringResource(id = R.string.camera_type_cameras)) },
            selected = cameraTypeState == CameraType.CAMERAS,
            onClick = {
                cameraTypeState = CameraType.CAMERAS
            }
        )
        Tab(text = { Text(stringResource(id = R.string.camera_type_doors)) },
            selected = cameraTypeState == CameraType.DOORS,
            onClick = {
                cameraTypeState = CameraType.DOORS
            }
        )
    }

    when (cameraTypeState) {
        CameraType.CAMERAS -> CamerasScreenHolder()
        CameraType.DOORS -> DoorsScreenHolder()
    }
}

@Composable
@Preview
fun PreviewMainScreen() {
    CamerasAppTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding(),
            color = MaterialTheme.colorScheme.background,
        ) {
            MainScreenHolder()
        }
    }
}