package com.android.cameras.app.di.modules

import com.android.cameras.app.data.network.NetworkRepoImpl
import com.android.cameras.app.domain.repo.NetworkRepo
import dagger.Binds
import dagger.Module
import dagger.Provides
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.features.json.GsonSerializer
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.logging.DEFAULT
import io.ktor.client.features.logging.LogLevel
import io.ktor.client.features.logging.Logger
import io.ktor.client.features.logging.Logging
import javax.inject.Singleton


@Module(includes = [NetworkModule.BindModule::class])
class NetworkModule {

    @Provides
    @Singleton
    fun getInstance() = HttpClient(Android) {
        install(JsonFeature) {
            serializer = GsonSerializer()
        }
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.ALL
        }
    }

    @Module
    internal interface BindModule {
        @Binds
        fun bindNetworkRepo(networkRepoImpl: NetworkRepoImpl): NetworkRepo
    }
}