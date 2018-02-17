package com.sumera.argallery.injection.modules

import com.sumera.argallery.injection.PerActivity
import com.sumera.argallery.ui.feature.main.MainActivity
import com.sumera.argallery.ui.feature.main.MainActivityModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilderModule {

    @PerActivity
    @ContributesAndroidInjector(modules = [MainActivityModule::class])
    abstract fun mainActivity(): MainActivity
}