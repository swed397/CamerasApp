package com.android.cameras.app.di

import com.android.cameras.app.data.bd.model.CameraBdModel
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
        val config = RealmConfiguration.Builder(schema = setOf(CameraBdModel::class)).build()

        return Realm.open(config)
    }
}