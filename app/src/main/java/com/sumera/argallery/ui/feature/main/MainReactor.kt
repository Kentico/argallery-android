package com.sumera.argallery.ui.feature.main

import com.sumera.argallery.data.store.datasource.model.DataSourceType
import com.sumera.argallery.domain.datasource.SetCurrentDataSourceCompletabler
import com.sumera.argallery.tools.behaviours.ExecuteBehaviour
import com.sumera.argallery.ui.base.BaseReactor
import com.sumera.argallery.ui.feature.main.contract.MainState
import com.sumera.argallery.ui.feature.main.contract.TabClickedAction
import com.sumera.koreactor.behaviour.completable
import com.sumera.koreactor.behaviour.triggers
import com.sumera.koreactor.reactor.data.MviAction
import io.reactivex.Observable
import javax.inject.Inject

class MainReactor @Inject constructor(
        private val setCurrentDataSourceCompletabler: SetCurrentDataSourceCompletabler
) : BaseReactor<MainState>() {

    override fun createInitialState() = MainState(
            dataSourceType = DataSourceType.ALL
    )

    override fun bind(actions: Observable<MviAction<MainState>>) {
        val dataSourceChanged = actions.ofActionType<TabClickedAction>()

        ExecuteBehaviour<TabClickedAction, MainState>(
                triggers = triggers(dataSourceChanged),
                worker = completable { setCurrentDataSourceCompletabler.init(it.dataSourceType).execute() }
        ).bindToView()
    }
}