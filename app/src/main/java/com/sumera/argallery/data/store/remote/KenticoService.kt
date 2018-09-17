package com.sumera.argallery.data.store.remote

import com.kenticocloud.delivery_core.interfaces.item.common.IQueryParameter
import com.sumera.argallery.data.store.remote.model.PictureModel
import io.reactivex.Single

interface KenticoService {
    fun getPictures(limit: Int, parameters: List<IQueryParameter>): Single<List<PictureModel>>
}