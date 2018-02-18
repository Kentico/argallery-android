package com.sumera.argallery.data.store.persistence

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.sumera.argallery.data.store.persistence.model.PersistedPicture

@Database(entities = [PersistedPicture::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    companion object {
        val name = "app_database"
    }

    abstract fun favouritePicturesDao(): FavouritePicturesDao
}