package com.sumera.argallery.ui.feature.main.contract

import com.sumera.argallery.data.store.ui.datasource.model.DataSourceType
import com.sumera.koreactor.reactor.data.MviStateReducer

sealed class MainReducers : MviStateReducer<MainState>

data class SetDataSourceType(
    private val dataSourceType: DataSourceType
) : MainReducers() {
    override fun reduce(oldState: MainState): MainState {
        return oldState.copy(dataSourceType = dataSourceType)
    }
}
