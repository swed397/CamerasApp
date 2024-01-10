package com.android.cameras.app

import android.app.Application
import com.android.cameras.app.data.CamerasDataRepoImpl
import com.android.cameras.app.data.DoorsDataRepoImpl
import com.android.cameras.app.di.AppComponent
import com.android.cameras.app.di.DaggerAppComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.inject.Inject

class App : Application() {

    lateinit var appComponent: AppComponent

    @Inject
    lateinit var camerasDataRepo: CamerasDataRepoImpl

    @Inject
    lateinit var doorsDataRepo: DoorsDataRepoImpl

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