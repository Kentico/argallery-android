package com.sumera.argallery.ui.feature.picturedetails.contract

import com.sumera.argallery.data.store.ui.model.Picture
import com.sumera.koreactor.reactor.data.MviState

data class PictureDetailsState(
        val pictures: List<Picture>,
        val initialPicture: Picture
) : MviState