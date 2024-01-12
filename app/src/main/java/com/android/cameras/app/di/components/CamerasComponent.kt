package com.android.cameras.app.di.components

import com.android.cameras.app.di.modules.CamerasInteractorModule
import com.android.cameras.app.di.scopes.CamerasScope
import com.android.cameras.app.ui.cameras.CamerasViewModel
import dagger.Subcomponent

@Subcomponent(modules = [CamerasInteractorModule::class])
@CamerasScope
interface CamerasComponent {

    val camerasViewModelFactory: CamerasViewModel.Factory

    @Subcomponent.Builder
    interface Builder {
        fun build(): CamerasComponent
    }
}