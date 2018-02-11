package com.sumera.argallery.injection.modules

import android.content.Context
import com.sumera.argallery.App
import com.sumera.argallery.injection.ApplicationContext
import dagger.Module
import dagger.Provides

@Module
class ApplicationModule {

    @Provides
    @ApplicationContext
    fun context(app: App): Context = app
}