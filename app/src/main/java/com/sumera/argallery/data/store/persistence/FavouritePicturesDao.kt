package com.sumera.argallery.data.store.persistence

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import android.arch.persistence.room.Query
import com.sumera.argallery.data.store.persistence.model.PersistedPicture
import io.reactivex.Flowable

@Dao
interface FavouritePicturesDao {

    @Query("SELECT * FROM picture")
    fun getAll(): Flowable<List<PersistedPicture>>

    @Query("SELECT COUNT(*) FROM picture WHERE id = :id")
    fun getItemsCountForId(id: String): Flowable<Int>

    @Insert(onConflict = REPLACE)
    fun insertPicture(picture: PersistedPicture)

    @Delete
    fun deletePicture(picture: PersistedPicture)
}