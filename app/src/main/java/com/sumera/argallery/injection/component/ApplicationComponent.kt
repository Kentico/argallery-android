package com.sumera.argallery.injection.component

import com.sumera.argallery.App
import com.sumera.argallery.injection.modules.ActivityBuilderModule
import com.sumera.argallery.injection.modules.ApplicationModule
import com.sumera.argallery.injection.modules.FragmentBuilderModule
import com.sumera.argallery.injection.modules.NetworkModule
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
    ApplicationModule::class,
    AndroidInjectionModule::class,
    AndroidSupportInjectionModule::class,
    ActivityBuilderModule::class,
    FragmentBuilderModule::class,
    NetworkModule::class
])
interface ApplicationComponent : AndroidInjector<App> {

    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<App>()
}