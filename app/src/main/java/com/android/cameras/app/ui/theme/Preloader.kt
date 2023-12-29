package com.android.cameras.app.ui.theme

import androidx.compose.foundation.layout.width
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun Preloader(modifier: Modifier = Modifier) {
    LinearProgressIndicator(
        modifier = modifier
            .width(64.dp),
        color = MaterialTheme.colorScheme.secondary
    )
}