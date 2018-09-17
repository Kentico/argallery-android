package com.sumera.argallery.data.store.remote

import com.kenticocloud.delivery_core.config.DeliveryConfig
import com.kenticocloud.delivery_core.interfaces.item.common.IQueryParameter
import com.kenticocloud.delivery_rx.DeliveryService
import com.sumera.argallery.data.store.remote.config.AppConfig
import com.sumera.argallery.data.store.remote.model.PictureModel
import io.reactivex.Single

class KenticoClient : KenticoService {

    override fun getPictures(limit: Int, parameters: List<IQueryParameter>): Single<List<PictureModel>> {
        val query = deliveryService.items<PictureModel>()
                .type(PictureModel.TYPE)
                .limitParameter(limit)

        for (parameter in parameters) query.addParameter(parameter)

        val items = query.get().items

        return Single.just(items)
    }

    companion object {
        private val config = DeliveryConfig(AppConfig.KENTICO_CLOUD_PROJECT_ID, AppConfig.getTypeResolvers())

        private val deliveryService = DeliveryService(config)
    }
}
