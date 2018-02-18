package com.sumera.argallery.data.mapper

import com.sumera.argallery.data.store.persistence.model.PersistedPicture
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

    fun toPictures(persistedPictures: List<PersistedPicture>): List<Picture> {
        return persistedPictures.map { item ->
            Picture(
                    id = item.id,
                    title = item.title,
                    author = item.author,
                    description = item.description,
                    imageUrl = item.imageUrl,
                    price = item.price
            )
        }
    }

    fun toPersistedPicture(picture: Picture): PersistedPicture {
        return PersistedPicture(
                id = picture.id,
                title = picture.title,
                author = picture.author,
                description = picture.description,
                imageUrl = picture.imageUrl,
                price = picture.price
        )
    }
}

