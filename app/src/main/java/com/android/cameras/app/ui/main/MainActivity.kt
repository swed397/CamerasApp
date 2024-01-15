package com.android.cameras.app.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.android.cameras.app.App
import com.android.cameras.app.di.injectedViewModel
import com.android.cameras.app.ui.theme.CamerasAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContent {
            val context = LocalContext.current.applicationContext
            val viewModel = injectedViewModel {
                (context as App).appComponent.mainViewModelFactory.create()
            }

            CamerasAppTheme(darkTheme = false) {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .systemBarsPadding(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreenHolder()
                }
            }
        }
    }
}