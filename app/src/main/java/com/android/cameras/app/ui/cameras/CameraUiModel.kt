package com.android.cameras.app.ui.cameras

import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color

data class CameraUiModel(
    val id: Long,
    val category: String,
    val name: String,
    val favorites: Boolean,
    @DrawableRes
    val favoritesIcon: Int,
    @ColorInt
    val favoritesIconColor: Color,
    val rec: Boolean,
    @DrawableRes
    val recIcon: Int,
    @ColorRes
    val recIconColor: Color,
    @DrawableRes
    val favoritesButtonIcon: Int,
    @ColorInt
    val favoritesButtonColor: Color,
    val imageUrl: String
)
