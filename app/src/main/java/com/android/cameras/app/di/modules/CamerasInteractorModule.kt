package com.android.cameras.app.di.modules

import com.android.cameras.app.data.CamerasDataInteractorImpl
import com.android.cameras.app.di.scopes.CamerasScope
import com.android.cameras.app.domain.CamerasDataInteractor
import dagger.Binds
import dagger.Module

@Module
interface CamerasInteractorModule {

    @Binds
    @CamerasScope
    fun bindCameraInteractor(camerasDataInteractor: CamerasDataInteractorImpl): CamerasDataInteractor
}