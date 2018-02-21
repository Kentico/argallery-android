package com.sumera.argallery.ui.feature.main

import android.os.Bundle
import android.transition.TransitionManager
import com.jakewharton.rxbinding2.support.design.widget.selections
import com.jakewharton.rxbinding2.view.clicks
import com.sumera.argallery.R
import com.sumera.argallery.data.store.ui.datasource.model.DataSourceType
import com.sumera.argallery.tools.extensions.setVisibile
import com.sumera.argallery.ui.base.BaseActivity
import com.sumera.argallery.ui.feature.filter.FilterFragment
import com.sumera.argallery.ui.feature.main.contract.MainState
import com.sumera.argallery.ui.feature.main.contract.NavigateToFilter
import com.sumera.argallery.ui.feature.main.contract.OnFilterClickedAction
import com.sumera.argallery.ui.feature.main.contract.OnTabClickedAction
import com.sumera.koreactor.reactor.MviReactor
import com.sumera.koreactor.reactor.data.MviEvent
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

        main_filterIcon.clicks()
                .map { OnFilterClickedAction }
                .bindToReactor()

        main_tabs.selections()
                .map { getTabDataSourceTypeFor(it.position) }
                .map { OnTabClickedAction(it) }
                .bindToReactor()
    }

    override fun onBackPressed() {
        if (supportFragmentManager.findFragmentByTag("tag") != null) {
            val fragment = supportFragmentManager.findFragmentByTag("tag") as? FilterFragment
            fragment?.close()
        } else {
            super.onBackPressed()
        }
    }

    override fun bindToState(stateObservable: Observable<MainState>) {
        stateObservable.getChange { it.dataSourceType }
                .map { getTabPositionFor(it) }
                .observeState { main_tabs.getTabAt(it)?.select() }

        stateObservable.getChange { it.dataSourceType }
                .map { it == DataSourceType.FILTERED }
                .observeState { isVisible ->
                    TransitionManager.beginDelayedTransition(main_toolbar)
                    main_filterIcon.setVisibile(isVisible)
                }
    }

    override fun bindToEvent(eventsObservable: Observable<MviEvent<MainState>>) {
        eventsObservable.observeEvent { event ->
            when(event) {
                NavigateToFilter -> {
                    val fm = supportFragmentManager
                    fm.beginTransaction()
//                            .setCustomAnimations(R.anim.slide_in_down, R.anim.slide_in_down, R.anim.slide_in_down, R.anim.slide_in_down)
                            .add(R.id.main_filterTabContainer, FilterFragment.newInstance(), "tag")
                            .commit()
                }
            }
        }
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
