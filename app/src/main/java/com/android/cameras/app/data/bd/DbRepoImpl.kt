package com.android.cameras.app.data.bd

import com.android.cameras.app.data.bd.model.CameraDbModel
import com.android.cameras.app.data.bd.model.DoorDbModel
import com.android.cameras.app.domain.CameraModel
import com.android.cameras.app.domain.DoorModel
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import javax.inject.Inject

class DbRepoImpl @Inject constructor(private val realm: Realm) {

    fun findAllCameras(): List<CameraModel> = realm.query<CameraDbModel>().find().map {
        CameraModel(
            id = it.apiId,
            category = it.category,
            name = it.name ?: "",
            favorites = it.favorites,
            rec = it.rec,
            imageUrl = it.imageUrl ?: ""
        )
    }

    fun findAllCamerasIds(): List<Long> = realm.query<CameraDbModel>().find().map { it.apiId }

    suspend fun saveAllCameras(data: List<CameraModel>) {
        realm.write {
            data.map {
                val entity = CameraDbModel().apply {
                    apiId = it.id
                    category = it.category
                    name = it.name
                    favorites = it.favorites
                    rec = it.rec
                    imageUrl = it.imageUrl
                }
                copyToRealm(entity)
            }
        }
    }

    suspend fun updateFavoriteCameraById(id: Long) {
        realm.write {
            val camera = query<CameraDbModel>("apiId == $0", id).find().first()
            camera.favorites = camera.favorites.not()
        }
    }

    suspend fun findAllDoors(): List<DoorModel> = realm.query<DoorDbModel>().find().map {
        DoorModel(
            name = it.name ?: "",
            room = it.room,
            id = it.apiId,
            favorites = it.favorites,
            imgUrl = it.imageUrl
        )
    }

    suspend fun saveAllDoors(data: List<DoorModel>) {
        realm.write {
            data.map {
                val entity = DoorDbModel().apply {
                    apiId = it.id
                    room = it.room
                    name = it.name
                    favorites = it.favorites
                    imageUrl = it.imgUrl
                }
                copyToRealm(entity)
            }
        }
    }

    suspend fun updateFavoriteDoorById(id: Long) {
        realm.write {
            val door = query<DoorDbModel>("apiId == $0", id).find().first()
            door.favorites = door.favorites.not()
        }
    }

    suspend fun updateNameDoorById(id: Long, name: String) {
        realm.write {
            val door = query<DoorDbModel>("apiId == $0", id).find().first()
            door.name = name
        }
    }

    fun findAllDoorsIds(): List<Long> = realm.query<DoorDbModel>().find().map { it.apiId }
}