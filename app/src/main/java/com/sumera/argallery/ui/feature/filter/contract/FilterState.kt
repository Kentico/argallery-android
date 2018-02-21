package com.sumera.argallery.ui.feature.filter.contract

import com.sumera.argallery.data.store.ui.model.Filter
import com.sumera.koreactor.reactor.data.MviState

data class FilterState(
        val currentFilter: Filter
) : MviState