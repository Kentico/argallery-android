package com.sumera.argallery.injection.modules

import android.support.v7.app.AppCompatActivity
import dagger.Module
import dagger.Provides

@Module
open class BaseActivityModule {

    @Provides
    fun context(appCompatActivity: AppCompatActivity) = appCompatActivity
}