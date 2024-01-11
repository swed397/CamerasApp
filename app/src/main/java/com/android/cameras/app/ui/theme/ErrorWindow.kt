package com.android.cameras.app.ui.theme

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp

@Composable
fun ErrorWindow(error: String) {
    Text(
        text = error,
        textAlign = TextAlign.Center,
        fontSize = 17.sp,
    )
}