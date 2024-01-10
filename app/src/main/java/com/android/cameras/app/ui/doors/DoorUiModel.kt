package com.android.cameras.app.ui.doors

import androidx.annotation.DrawableRes

data class DoorUiModel(
    val id: Long,
    val room: String,
    val name: String,
    val favorites: Boolean,
    @DrawableRes
    val favoritesButtonIcon: Int,
    val imageUrl: String?,
    val borderCornerShapePercent: Int
)
