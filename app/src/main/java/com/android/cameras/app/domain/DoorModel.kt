package com.android.cameras.app.domain

data class DoorModel(
    val name: String,
    val room: String?,
    val id: Long,
    val favorites: Boolean,
    val imgUrl: String?
)
