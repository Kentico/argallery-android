package com.sumera.argallery.data.mapper

import com.sumera.argallery.data.store.persistence.model.PersistedPicture
import com.sumera.argallery.data.store.ui.model.Picture
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class PictureMapperTest {

    lateinit var pictureMapper: PictureMapper

    @Before
    fun setUp() {
        pictureMapper = PictureMapper()
    }

    @Test
    fun `toPictures from persisted pictures should return pictures`() {
        // GIVEN
        val persistedPictures = listOf(createPersistedPicture(id = "1"), createPersistedPicture(id = "2"))

        // WHEN
        val result = pictureMapper.toPictures(persistedPictures)

        // THEN
        assertEquals(listOf(createPicture(id = "1"), createPicture(id = "2")), result)
    }

    @Test
    fun `toPersistedPicture should return persisted picture`() {
        // GIVEN
        val picture = createPicture(id = "1")

        // WHEN
        val result = pictureMapper.toPersistedPicture(picture)

        // THEN
        assertEquals(createPersistedPicture(id = "1"), result)
    }

    private fun createPicture(
            id: String = "id",
            title: String = "title",
            author: String = "author",
            description: String = "descr",
            imageUrl: String = "url",
            price: Int = 100
    ): Picture {
        return Picture(
                id = id,
                title = title,
                author = author,
                description = description,
                imageUrl = imageUrl,
                price = price
        )
    }

    private fun createPersistedPicture(
            id: String = "id",
            title: String = "title",
            author: String = "author",
            description: String = "descr",
            imageUrl: String = "url",
            price: Int = 100
    ): PersistedPicture {
        return PersistedPicture(
                id = id,
                title = title,
                author = author,
                description = description,
                imageUrl = imageUrl,
                price = price
        )
    }
}