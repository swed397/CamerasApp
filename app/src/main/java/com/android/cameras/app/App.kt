package com.android.cameras.app

import android.app.Application
import com.android.cameras.app.domain.interactors.impl.CamerasDataInteractorImpl
import com.android.cameras.app.domain.interactors.impl.DoorsDataInteractorImpl
import com.android.cameras.app.di.components.AppComponent
import com.android.cameras.app.di.components.DaggerAppComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.inject.Inject

class App : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder().build()
    }
}