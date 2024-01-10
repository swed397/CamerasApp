package com.android.cameras.app.data.network

import com.android.cameras.app.domain.CameraModel
import com.android.cameras.app.domain.DoorModel
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.http.takeFrom
import javax.inject.Inject

class NetworkRepoImpl @Inject constructor(private val httpClient: HttpClient) {

    suspend fun getAllCameras(): List<CameraModel> = httpClient.get<CamerasNetworkModel> {
        url.takeFrom("$BASE_URL/cameras/")
    }.data.camerasList
        .map {
            CameraModel(
                id = it.id,
                name = it.name,
                category = it.room,
                imageUrl = it.imgUrl,
                favorites = it.favorites,
                rec = it.rec
            )
        }

    suspend fun getAllDoors(): List<DoorModel> = httpClient.get<DoorsNetworkModel> {
        url.takeFrom("$BASE_URL/doors/")
    }.data.map {
        DoorModel(
            id = it.id,
            room = it.room,
            name = it.name,
            favorites = it.favorites,
            imgUrl = it.imgUrl
        )
    }

    suspend fun finAllIds(): List<Long> = httpClient.get<CamerasNetworkModel> {
        url.takeFrom("$BASE_URL/cameras/")
    }.data.camerasList.map { it.id }


    private companion object {
        const val BASE_URL = "https://cars.cprogroup.ru/api/rubetek"
    }
}