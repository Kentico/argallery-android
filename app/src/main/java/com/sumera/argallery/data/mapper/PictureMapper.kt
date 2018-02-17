package com.sumera.argallery.data.mapper

import com.sumera.argallery.data.store.remote.model.PictureResponse
import com.sumera.argallery.data.store.ui.model.Picture
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PictureMapper @Inject constructor() {

    fun toPictures(pictureResponse: PictureResponse): List<Picture> {
        return pictureResponse.items.map { item ->
            Picture(
                    id = item.system.id,
                    title = item.elements.title.value,
                    author = item.elements.author.value,
                    description = item.elements.description.value,
                    imageUrl = item.elements.image.value.first().url,
                    price = item.elements.price.value
            )
        }
    }
}

