package com.android.cameras.app.data.bd

import com.android.cameras.app.data.bd.model.CameraBdModel
import com.android.cameras.app.domain.CameraModel
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import javax.inject.Inject

class BdRepoImpl @Inject constructor(private val realm: Realm) {

    fun findAll(): List<CameraModel> = realm.query<CameraBdModel>().find().map {
        CameraModel(
            id = it.apiId,
            category = it.category,
            name = it.name ?: "",
            favorites = it.favorites,
            rec = it.rec,
            imageUrl = it.imageUrl ?: ""
        )
    }

    fun findAllIds(): List<Long> = realm.query<CameraBdModel>().find().map { it.apiId }

    suspend fun saveAll(data: List<CameraModel>) {
        realm.write {
            data.map {
                val entity = CameraBdModel().apply {
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
}