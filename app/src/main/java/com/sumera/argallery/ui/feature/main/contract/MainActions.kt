package com.sumera.argallery.ui.feature.main.contract

import com.sumera.argallery.data.store.ui.datasource.model.DataSourceType
import com.sumera.koreactor.reactor.data.MviAction

sealed class MainActions : MviAction<MainState>

data class TabClickedAction(val dataSourceType: DataSourceType) : MainActions()