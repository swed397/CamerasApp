package com.android.cameras.app.di.modules

import com.android.cameras.app.data.bd.DbRepoImpl
import com.android.cameras.app.data.bd.model.CameraDbModel
import com.android.cameras.app.data.bd.model.DoorDbModel
import com.android.cameras.app.data.network.NetworkRepoImpl
import com.android.cameras.app.domain.repo.DbRepo
import com.android.cameras.app.domain.repo.NetworkRepo
import dagger.Binds
import dagger.Module
import dagger.Provides
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import javax.inject.Singleton

@Module(includes = [BdModule.BindModule::class])
class BdModule {

    @Provides
    @Singleton
    fun provideRealm(): Realm {
        val config = RealmConfiguration
            .Builder(schema = setOf(CameraDbModel::class, DoorDbModel::class))
            .build()

        return Realm.open(config)
    }

    @Module
    internal interface BindModule {
        @Binds
        fun bindDbRepo(dbRepoImpl: DbRepoImpl): DbRepo
    }
}