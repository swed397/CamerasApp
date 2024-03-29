package com.android.cameras.app.domain.models

data class CameraModel(
    val id: Long,
    val category: String?,
    val name: String,
    val favorites: Boolean,
    val rec: Boolean,
    val imageUrl: String
)
