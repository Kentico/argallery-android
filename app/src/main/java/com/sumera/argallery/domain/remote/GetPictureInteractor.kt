package com.sumera.argallery.domain.remote

import com.sumera.argallery.data.remote.model.ContentItems
import com.sumera.argallery.data.store.KenticoStore
import com.sumera.argallery.domain.base.BaseSingleInteractor
import io.reactivex.Single
import javax.inject.Inject

class GetPictureInteractor @Inject constructor(
        private val kenticoStore: KenticoStore
) : BaseSingleInteractor<ContentItems>() {

    override fun create(): Single<ContentItems> {
        return kenticoStore.getPictures()
    }
}