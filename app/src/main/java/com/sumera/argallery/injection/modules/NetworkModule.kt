package com.sumera.argallery.injection.modules

import com.sumera.argallery.BuildConfig
import com.sumera.argallery.data.store.remote.KenticoService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import javax.inject.Singleton

@Module
class NetworkModule {

    @Provides
    @Singleton
    fun kenticoService():
            KenticoService {
        return Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .validateEagerly(BuildConfig.DEBUG)
                .build()
                .create(KenticoService::class.java)
    }
}
