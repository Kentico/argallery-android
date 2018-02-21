package com.sumera.argallery.data.store.remote

import com.sumera.argallery.data.store.remote.model.PictureResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface KenticoService {

    @GET("items?system.type=picture")
    fun getPictures(
            @Query("limit") limit: Int,
            @Query("skip") skip: Int,
            @QueryMap(encoded = false) options: Map<String, String>
    ): Single<PictureResponse>
}