package com.android.cameras.app.data.bd

import com.android.cameras.app.data.bd.model.CameraDbModel
import com.android.cameras.app.data.bd.model.DoorDbModel
import com.android.cameras.app.domain.models.CameraModel
import com.android.cameras.app.domain.models.DoorModel
import com.android.cameras.app.domain.repo.DbRepo
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import javax.inject.Inject

class DbRepoImpl @Inject constructor(private val realm: Realm) : DbRepo {

    override suspend fun findAllCameras(): List<CameraModel> =
        realm.query<CameraDbModel>().find().map {
            CameraModel(
                id = it.apiId,
                category = it.category,
                name = it.name ?: "",
                favorites = it.favorites,
                rec = it.rec,
                imageUrl = it.imageUrl ?: ""
            )
        }

    override suspend fun findAllCamerasIds(): List<Long> =
        realm.query<CameraDbModel>().find().map { it.apiId }

    override suspend fun saveAllCameras(data: List<CameraModel>) {
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

    override suspend fun updateFavoriteCameraById(id: Long) {
        realm.write {
            val camera = query<CameraDbModel>("apiId == $0", id).find().first()
            camera.favorites = camera.favorites.not()
        }
    }

    override suspend fun findAllDoors(): List<DoorModel> = realm.query<DoorDbModel>().find().map {
        DoorModel(
            name = it.name ?: "",
            room = it.room,
            id = it.apiId,
            favorites = it.favorites,
            imgUrl = it.imageUrl
        )
    }

    override suspend fun saveAllDoors(data: List<DoorModel>) {
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

    override suspend fun updateFavoriteDoorById(id: Long) {
        realm.write {
            val door = query<DoorDbModel>("apiId == $0", id).find().first()
            door.favorites = door.favorites.not()
        }
    }

    override suspend fun updateNameDoorById(id: Long, name: String) {
        realm.write {
            val door = query<DoorDbModel>("apiId == $0", id).find().first()
            door.name = name
        }
    }

    override suspend fun findAllDoorsIds(): List<Long> =
        realm.query<DoorDbModel>().find().map { it.apiId }
}