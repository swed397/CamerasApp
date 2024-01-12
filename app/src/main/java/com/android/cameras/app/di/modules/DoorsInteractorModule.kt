package com.android.cameras.app.di.modules

import com.android.cameras.app.data.DoorsDataInteractorImpl
import com.android.cameras.app.di.scopes.CamerasScope
import com.android.cameras.app.di.scopes.DoorsScope
import com.android.cameras.app.domain.DoorsDataInteractor
import dagger.Binds
import dagger.Module

@Module
interface DoorsInteractorModule {

    @Binds
    @DoorsScope
    fun bindDoorsInteractor(doorsInteractor: DoorsDataInteractorImpl): DoorsDataInteractor
}