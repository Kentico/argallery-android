package com.sumera.argallery.ui.feature.main.contract

import com.sumera.koreactor.reactor.data.MviEvent

sealed class MainEvents : MviEvent<MainState>()

object NavigateToFilter : MainEvents()