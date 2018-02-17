package com.sumera.argallery.data.store.remote.model

import com.squareup.moshi.Json

data class PictureResponse(
        @Json(name = "items") val items: List<PictureItems>
)

data class PictureItems(
        @Json(name = "system") val system: System,
        @Json(name = "elements") val elements: PictureElements
)

data class PictureElements(
        @Json(name = "title") val title: TextElement,
        @Json(name = "author") val author: TextElement,
        @Json(name = "description") val description: TextElement,
        @Json(name = "picture") val image: AssetElement,
        @Json(name = "price") val price: NumberElement
)