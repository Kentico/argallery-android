package com.sumera.argallery.ui.main

import android.app.Activity
import com.sumera.argallery.injection.PerActivity
import dagger.Binds
import dagger.Module

@Module
abstract class MainActivityModule {

    @Binds
    @PerActivity
    abstract fun activity(mainActivity: MainActivity): Activity
}