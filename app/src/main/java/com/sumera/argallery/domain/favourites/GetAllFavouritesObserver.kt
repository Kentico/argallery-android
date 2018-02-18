package com.sumera.argallery.domain.favourites

import com.sumera.argallery.data.mapper.PictureMapper
import com.sumera.argallery.data.store.persistence.AppDatabase
import com.sumera.argallery.data.store.ui.model.Picture
import com.sumera.argallery.domain.base.BaseObserver
import io.reactivex.Observable
import javax.inject.Inject

class GetAllFavouritesObserver @Inject constructor(
        private val appDatabase: AppDatabase,
        private val mapper: PictureMapper
): BaseObserver<List<Picture>>() {

    override fun create(): Observable<List<Picture>> {
        return appDatabase.favouritePicturesDao()
                .getAll()
                .toObservable()
                .map { mapper.toPictures(it) }
    }
}