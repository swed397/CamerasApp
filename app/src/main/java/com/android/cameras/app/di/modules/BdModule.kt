package com.android.cameras.app.di.modules

import com.android.cameras.app.data.bd.model.CameraDbModel
import com.android.cameras.app.data.bd.model.DoorDbModel
import dagger.Module
import dagger.Provides
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import javax.inject.Singleton

@Module
class BdModule {

    @Provides
    @Singleton
    fun provideRealm(): Realm {
        val config = RealmConfiguration
            .Builder(schema = setOf(CameraDbModel::class, DoorDbModel::class))
            .build()

        return Realm.open(config)
    }


}