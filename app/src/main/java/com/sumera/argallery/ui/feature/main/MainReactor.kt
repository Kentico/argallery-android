package com.sumera.argallery.ui.feature.main

import com.sumera.argallery.data.store.ui.datasource.model.DataSourceType
import com.sumera.argallery.domain.datasource.GetCurrentDataSourceTypeObserver
import com.sumera.argallery.domain.datasource.SetCurrentDataSourceCompletabler
import com.sumera.argallery.tools.DEFAULT_DATA_SOURCE
import com.sumera.argallery.tools.koreactor.ExecuteBehaviour
import com.sumera.argallery.tools.koreactor.ObserveBehaviour
import com.sumera.argallery.ui.base.BaseReactor
import com.sumera.argallery.ui.feature.main.contract.MainState
import com.sumera.argallery.ui.feature.main.contract.NavigateToFilter
import com.sumera.argallery.ui.feature.main.contract.OnFilterClickedAction
import com.sumera.argallery.ui.feature.main.contract.OnTabClickedAction
import com.sumera.argallery.ui.feature.main.contract.SetDataSourceType
import com.sumera.koreactor.behaviour.completable
import com.sumera.koreactor.behaviour.messages
import com.sumera.koreactor.behaviour.observable
import com.sumera.koreactor.behaviour.triggers
import com.sumera.koreactor.reactor.data.MviAction
import io.reactivex.Observable
import javax.inject.Inject

class MainReactor @Inject constructor(
        private val setCurrentDataSourceCompletabler: SetCurrentDataSourceCompletabler,
        private val getCurrentDataSourceTypeObserver: GetCurrentDataSourceTypeObserver
) : BaseReactor<MainState>() {

    override fun createInitialState() = MainState(
            dataSourceType = DEFAULT_DATA_SOURCE
    )

    override fun bind(actions: Observable<MviAction<MainState>>) {
        val onTabClickedAction = actions.ofActionType<OnTabClickedAction>()
        val onFilterClickedAction = actions.ofActionType<OnFilterClickedAction>()

        ObserveBehaviour<Any, DataSourceType, MainState>(
                triggers = triggers(attachLifecycleObservable),
                worker = observable { getCurrentDataSourceTypeObserver.execute() },
                message = messages { SetDataSourceType(it) }
        ).bindToView()

        ExecuteBehaviour<OnTabClickedAction, MainState>(
                triggers = triggers(onTabClickedAction),
                worker = completable { setCurrentDataSourceCompletabler.init(it.dataSourceType).execute() }
        ).bindToView()

        onFilterClickedAction
                .map { NavigateToFilter }
                .bindToView()
    }
}