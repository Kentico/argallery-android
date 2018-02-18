package com.sumera.argallery.domain.favourites

import com.sumera.argallery.data.store.persistence.AppDatabase
import com.sumera.argallery.domain.base.BaseObserver
import io.reactivex.Observable
import javax.inject.Inject

class IsFavouriteObserver @Inject constructor(
        private val appDatabase: AppDatabase
) : BaseObserver<Boolean>() {

    private lateinit var id: String

    fun init(id: String): IsFavouriteObserver {
        this.id = id
        return this
    }

    override fun create(): Observable<Boolean> {
        return appDatabase.favouritePicturesDao()
                .getItemsCountForId(id)
                .map { it > 0 }
                .toObservable()
    }
}