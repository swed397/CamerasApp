package com.android.cameras.app.data.network

import com.android.cameras.app.domain.CameraModel
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.http.takeFrom
import javax.inject.Inject

class NetworkRepoImpl @Inject constructor(private val httpClient: HttpClient) {

    suspend fun getAllCameras(): Map<String, List<CameraModel>> = httpClient.get<NetworkModel> {
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
        }.groupBy { it.category ?: "" }

    private companion object {
        const val BASE_URL = "https://cars.cprogroup.ru/api/rubetek"
    }
}