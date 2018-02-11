package com.sumera.argallery.injection.modules

import android.content.Context
import com.squareup.moshi.Moshi
import com.sumera.argallery.App
import com.sumera.argallery.injection.ApplicationContext
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ApplicationModule {

    @Provides
    @ApplicationContext
    fun context(app: App): Context = app

    @Provides
    @Singleton
    fun moshi(): Moshi {
        return Moshi.Builder().build()
    }
}