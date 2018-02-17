package com.sumera.argallery.ui.feature.main

import android.os.Bundle
import com.jakewharton.rxbinding2.support.design.widget.selections
import com.sumera.argallery.R
import com.sumera.argallery.data.store.datasource.model.DataSourceType
import com.sumera.argallery.ui.base.BaseActivity
import com.sumera.argallery.ui.feature.main.contract.MainState
import com.sumera.argallery.ui.feature.main.contract.TabClickedAction
import com.sumera.koreactor.reactor.MviReactor
import com.sumera.koreactor.util.extension.getChange
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : BaseActivity<MainState>() {

    @Inject lateinit var reactorFactory: MainReactorFactory

    override val layoutRes = R.layout.activity_main

    override fun createReactor(): MviReactor<MainState> {
       return getReactor(reactorFactory, MainReactor::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        main_tabs.selections()
                .map { getTabDataSourceTypeFor(it.position) }
                .map { TabClickedAction(it) }
                .bindToReactor()
    }

    override fun bindToState(stateObservable: Observable<MainState>) {
        stateObservable
                .getChange { it.dataSourceType }
                .map { getTabPositionFor(it) }
                .observeState { main_tabs.getTabAt(it)?.select() }
    }

    private fun getTabPositionFor(dataSourceType: DataSourceType): Int {
        return when(dataSourceType) {
            DataSourceType.ALL -> 0
            DataSourceType.FAVOURITES -> 1
            DataSourceType.FILTERED -> 2
        }
    }

    private fun getTabDataSourceTypeFor(position: Int): DataSourceType {
        return when(position) {
            0 -> DataSourceType.ALL
            1 -> DataSourceType.FAVOURITES
            2 -> DataSourceType.FILTERED
            else -> throw NotImplementedError()
        }
    }
}
