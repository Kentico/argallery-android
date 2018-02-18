package com.sumera.argallery.ui.feature.main.contract

import com.sumera.argallery.data.store.ui.datasource.model.DataSourceType
import com.sumera.koreactor.reactor.data.MviState

data class MainState(
        val dataSourceType: DataSourceType
) : MviState
