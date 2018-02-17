package com.sumera.argallery.ui.feature.picturelist.contract

import com.sumera.argallery.ui.feature.main.contract.MainState
import com.sumera.koreactor.reactor.data.MviEvent

sealed class PictureListEvents : MviEvent<MainState>()