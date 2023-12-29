package com.android.cameras.app.di

import com.android.cameras.app.ui.cameras.CamerasViewModel
import dagger.Component
import javax.inject.Singleton

@Component(modules = [NetworkModule::class, BdModule::class])
@Singleton
interface AppComponent {

    val camerasViewModelFactory: CamerasViewModel.Factory
}