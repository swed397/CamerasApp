package com.android.cameras.app.data.bd.model

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId


class CameraBdModel : RealmObject {

    @PrimaryKey
    var _id: ObjectId = ObjectId()
    var apiId: Long = 0
    var category: String? = null
    var name: String? = null
    var favorites: Boolean = false
    var rec: Boolean = false
    var imageUrl: String? = null
}