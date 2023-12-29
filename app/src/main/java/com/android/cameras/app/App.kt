package com.android.cameras.app

import android.app.Application
import com.android.cameras.app.di.AppComponent
import com.android.cameras.app.di.BdModule
import com.android.cameras.app.di.DaggerAppComponent

class App : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder().build()
    }
}