package com.sumera.argallery.ui.feature.picturelist.contract

import com.sumera.argallery.ui.feature.main.contract.PictureListState
import com.sumera.koreactor.reactor.data.MviEvent

sealed class PictureListEvents : MviEvent<PictureListState>()

object NavigateToPictureDetails : PictureListEvents()