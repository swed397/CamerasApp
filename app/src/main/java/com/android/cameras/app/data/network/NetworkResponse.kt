package com.android.cameras.app.data.network

import com.google.gson.annotations.SerializedName

data class CamerasNetworkModel(

    @SerializedName("data")
    val data: CameraData
)

data class CameraData(

    @SerializedName("room")
    val roomsList: List<String>,

    @SerializedName("cameras")
    val camerasList: List<Camera>
)

data class Camera(

    @SerializedName("name")
    val name: String,

    @SerializedName("snapshot")
    val imgUrl: String,

    @SerializedName("room")
    val room: String?,

    @SerializedName("id")
    val id: Long,

    @SerializedName("favorites")
    val favorites: Boolean,

    @SerializedName("rec")
    val rec: Boolean
)

data class DoorsNetworkModel(

    @SerializedName("data")
    val data: List<Doors>
)

data class Doors(
    @SerializedName("name")
    val name: String,

    @SerializedName("room")
    val room: String?,

    @SerializedName("id")
    val id: Long,

    @SerializedName("favorites")
    val favorites: Boolean,

    @SerializedName("snapshot")
    val imgUrl: String?,
)

