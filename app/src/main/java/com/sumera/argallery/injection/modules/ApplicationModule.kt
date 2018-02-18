package com.sumera.argallery.injection.modules

import android.arch.persistence.room.Room
import android.content.Context
import com.sumera.argallery.App
import com.sumera.argallery.data.store.persistence.AppDatabase
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
    fun database(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, AppDatabase.name)
                .fallbackToDestructiveMigration()
                .build()
    }
}