package com.sumera.argallery.ui.feature.picturedetail.contract

import com.sumera.koreactor.reactor.data.MviEvent

sealed class PictureDetailEvents : MviEvent<PictureDetailState>()

object NavigateToAugmentedReality : PictureDetailEvents()