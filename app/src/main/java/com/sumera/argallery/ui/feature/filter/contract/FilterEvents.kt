package com.sumera.argallery.ui.feature.filter.contract

import com.sumera.koreactor.reactor.data.MviEvent

sealed class FilterEvents : MviEvent<FilterState>()

object CloseEvent : FilterEvents()