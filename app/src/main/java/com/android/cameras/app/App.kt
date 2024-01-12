package com.android.cameras.app

import android.app.Application
import com.android.cameras.app.data.CamerasDataInteractorImpl
import com.android.cameras.app.data.DoorsDataInteractorImpl
import com.android.cameras.app.di.components.AppComponent
import com.android.cameras.app.di.components.DaggerAppComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.inject.Inject

class App : Application() {

    lateinit var appComponent: AppComponent

    @Inject
    lateinit var camerasDataRepo: CamerasDataInteractorImpl

    @Inject
    lateinit var doorsDataRepo: DoorsDataInteractorImpl

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder().build()
        appComponent.inject(this)

        populateBd()
    }

    private fun populateBd() {
        CoroutineScope(Dispatchers.IO).launch {
            camerasDataRepo.populateBdData()
            doorsDataRepo.populateBdData()
            cancel()
        }
    }
}