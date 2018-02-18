package com.sumera.argallery.ui.feature.picturedetails.contract

import com.sumera.koreactor.reactor.data.MviAction

sealed class PictureDetailsActions : MviAction<PictureDetailsState>

data class OnPictureChanged(val newPictureIndex: Int) : PictureDetailsActions()