package com.sumera.argallery.ui.feature.picturedetails.contract

import com.sumera.koreactor.reactor.data.MviEvent

sealed class PictureDetailsEvents : MviEvent<PictureDetailsState>()

data class ScrollToIndexEvent(val index: Int) : PictureDetailsEvents()