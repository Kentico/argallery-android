package com.sumera.argallery.data.store.remote

import com.sumera.argallery.data.mapper.PictureMapper
import com.sumera.argallery.data.store.ui.model.Picture
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class KenticoStore @Inject constructor(
        private val kenticoService: KenticoService,
        private val pictureMapper: PictureMapper
) {

    fun getPictures(limit: Int, skip: Int): Single<List<Picture>> {
        return kenticoService
                .getPictures(limit, skip)
                .map { pictureMapper.toPictures(it) }
    }
}