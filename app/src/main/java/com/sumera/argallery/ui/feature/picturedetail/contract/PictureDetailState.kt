package com.sumera.argallery.ui.feature.picturedetail.contract

import com.sumera.argallery.data.store.ui.model.Picture
import com.sumera.koreactor.reactor.data.MviState

data class PictureDetailState(
        val picture: Picture,
        val isFavourite: Boolean
) : MviState