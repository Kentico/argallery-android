package com.sumera.argallery.data.store.remote.model

import com.squareup.moshi.Json

data class TextElement(
        @Json(name = "value") val value: String
)

data class NumberElement(
        @Json(name = "value") val value: Int
)

data class AssetElement(
        @Json(name = "value") val value: List<Asset>
)

data class Asset(
        @Json(name = "url") val url: String
)

data class System(
        @Json(name = "id") val id: String
)