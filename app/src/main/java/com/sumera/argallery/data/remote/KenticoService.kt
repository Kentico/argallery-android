package com.sumera.argallery.data.remote

import com.sumera.argallery.data.remote.model.ContentItems
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface KenticoService {

    @GET("items?system.type=picture")
    fun getPictures(
            @Query("limit") limit: Int,
            @Query("skip") skip: Int
    ): Single<ContentItems>
}