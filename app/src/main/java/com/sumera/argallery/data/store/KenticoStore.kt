package com.sumera.argallery.data.store

import com.sumera.argallery.data.remote.KenticoService
import com.sumera.argallery.data.remote.model.ContentItems
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class KenticoStore @Inject constructor(
        private val kenticoService: KenticoService
) {

    fun getPictures(): Single<ContentItems> {
        return kenticoService.getPictures(10, 1)
    }
}