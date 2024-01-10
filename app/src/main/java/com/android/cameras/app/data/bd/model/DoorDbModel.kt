package com.android.cameras.app.data.bd.model

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId


class DoorDbModel : RealmObject {

    @PrimaryKey
    var _id: ObjectId = ObjectId()
    var apiId: Long = 0
    var name: String? = null
    var room: String? = null
    var favorites: Boolean = false
    var imageUrl: String? = null
}