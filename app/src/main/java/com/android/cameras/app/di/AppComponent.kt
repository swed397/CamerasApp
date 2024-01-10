package com.android.cameras.app.di

import com.android.cameras.app.App
import com.android.cameras.app.ui.cameras.CamerasViewModel
import com.android.cameras.app.ui.doors.DoorsViewModel
import dagger.Component
import javax.inject.Singleton

@Component(modules = [NetworkModule::class, BdModule::class])
@Singleton
interface AppComponent {

    val camerasViewModelFactory: CamerasViewModel.Factory
    val doorsViewModelFactory: DoorsViewModel.Factory

    fun inject(app: App)
}