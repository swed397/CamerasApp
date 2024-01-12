package com.android.cameras.app.di.components

import com.android.cameras.app.di.modules.DoorsInteractorModule
import com.android.cameras.app.di.scopes.DoorsScope
import com.android.cameras.app.ui.doors.DoorsViewModel
import dagger.Subcomponent

@Subcomponent(modules = [DoorsInteractorModule::class])
@DoorsScope
interface DoorsComponent {

    val doorsViewModelFactory: DoorsViewModel.Factory

    @Subcomponent.Builder
    interface Builder {
        fun build(): DoorsComponent
    }
}