package com.sumera.argallery.data.store.remote

import com.kenticocloud.delivery_core.interfaces.item.common.IQueryParameter
import com.sumera.argallery.data.mapper.PictureMapper
import com.sumera.argallery.data.store.ui.model.Picture
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class KenticoStore @Inject constructor(
        private val pictureMapper: PictureMapper
) {
    private var kenticoClient = KenticoClient()

    fun getPictures(limit: Int, parameters: List<IQueryParameter>): Single<List<Picture>> {
        return kenticoClient
                .getPictures(limit, parameters)
                .map { pictureMapper.toParcelablePictures(it) }
    }
}