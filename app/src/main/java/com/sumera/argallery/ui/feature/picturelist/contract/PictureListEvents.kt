package com.sumera.argallery.ui.feature.picturelist.contract

import com.sumera.argallery.data.store.ui.model.Picture
import com.sumera.koreactor.reactor.data.MviEvent

sealed class PictureListEvents : MviEvent<PictureListState>()

data class NavigateToPictureDetails(val picture: Picture) : PictureListEvents()