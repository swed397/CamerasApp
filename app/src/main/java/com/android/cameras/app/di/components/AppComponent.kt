package com.android.cameras.app.di.components

import com.android.cameras.app.App
import com.android.cameras.app.di.modules.BdModule
import com.android.cameras.app.di.modules.NetworkModule
import com.android.cameras.app.ui.cameras.CamerasViewModel
import com.android.cameras.app.ui.main.MainViewModel
import dagger.Component
import javax.inject.Singleton

@Component(modules = [NetworkModule::class, BdModule::class])
@Singleton
interface AppComponent {

    val mainViewModelFactory: MainViewModel.Factory

    fun camerasComponent(): CamerasComponent.Builder
    fun doorsComponent(): DoorsComponent.Builder
}