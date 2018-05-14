package com.sumera.argallery.ui.feature.picturedetail.contract

import com.sumera.koreactor.reactor.data.MviAction

sealed class PictureDetailActions : MviAction<PictureDetailState>

object ToggleFavouriteAction : PictureDetailActions()

object AugmentedRealityClicked : PictureDetailActions()