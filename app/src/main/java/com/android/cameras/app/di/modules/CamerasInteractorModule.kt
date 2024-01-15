package com.android.cameras.app.di.modules

import com.android.cameras.app.domain.interactors.impl.CamerasDataInteractorImpl
import com.android.cameras.app.di.scopes.CamerasScope
import com.android.cameras.app.domain.interactors.CamerasDataInteractor
import dagger.Binds
import dagger.Module

@Module
interface CamerasInteractorModule {

    @Binds
    @CamerasScope
    fun bindCameraInteractor(camerasDataInteractor: CamerasDataInteractorImpl): CamerasDataInteractor
}