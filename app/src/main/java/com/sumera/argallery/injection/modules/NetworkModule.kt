package com.sumera.argallery.injection.modules

import com.github.ajalt.timberkt.Timber
import com.squareup.moshi.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi
import com.sumera.argallery.BuildConfig
import com.sumera.argallery.data.remote.KenticoService
import com.sumera.argallery.injection.KenticoDeliveryUrl
import dagger.Module
import dagger.Provides
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
class NetworkModule {

    @Provides
    @Singleton
    fun loggingInterceptor(): Interceptor {
        return HttpLoggingInterceptor { message ->
            Timber.tag("OkHttp").d(message)
        }.setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @Provides
    @Singleton
    fun okHttpClient(interceptor: Interceptor): OkHttpClient {
        val builder = OkHttpClient.Builder()
        builder.addInterceptor(interceptor)
        return builder.build()
    }

    @Provides
    @Singleton
    fun kenticoService(@KenticoDeliveryUrl httpUrl: HttpUrl, okHttpClient: OkHttpClient, moshi: Moshi):
            KenticoService {
        return Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(httpUrl)
                .validateEagerly(BuildConfig.DEBUG)
                .build()
                .create(KenticoService::class.java)
    }

    @Provides
    @Singleton
    @KenticoDeliveryUrl
    fun kenticoDeliveryUrl(): HttpUrl {
        val url = HttpUrl.parse("https://deliver.kenticocloud.com/17bbb0c7-e46c-45ab-b1d2-177ab5f9244f/")
        return url ?: throw IllegalStateException("Invalid url")
    }

    @Provides
    @Singleton
    fun moshi(): Moshi {
        return Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()
    }
}