package com.sumera.argallery.ui.feature.filter

import com.sumera.argallery.data.store.ui.model.Filter
import com.sumera.argallery.domain.filter.GetCurrentFilterSingler
import com.sumera.argallery.domain.filter.SetCurrentFilterCompletabler
import com.sumera.argallery.tools.koreactor.ObserveBehaviour
import com.sumera.argallery.ui.feature.filter.contract.CloseEvent
import com.sumera.argallery.ui.feature.filter.contract.FilterState
import com.sumera.argallery.ui.feature.filter.contract.OnBackButtonClicked
import com.sumera.argallery.ui.feature.filter.contract.OnCloseAreaClicked
import com.sumera.argallery.ui.feature.filter.contract.OnCloseButtonClicked
import com.sumera.argallery.ui.feature.filter.contract.OnFirstCategoryStateChanged
import com.sumera.argallery.ui.feature.filter.contract.OnPriceRangeChanged
import com.sumera.argallery.ui.feature.filter.contract.OnResetButtonClicked
import com.sumera.argallery.ui.feature.filter.contract.OnSecondCategoryStateChanged
import com.sumera.argallery.ui.feature.filter.contract.OnThirdCategoryStateChanged
import com.sumera.argallery.ui.feature.filter.contract.OnYearRangeChanged
import com.sumera.argallery.ui.feature.filter.contract.SetCurrentFilter
import com.sumera.argallery.ui.feature.filter.contract.SetFirstCategoryState
import com.sumera.argallery.ui.feature.filter.contract.SetPriceRangeReducer
import com.sumera.argallery.ui.feature.filter.contract.SetSecondCategoryState
import com.sumera.argallery.ui.feature.filter.contract.SetThirdCategoryState
import com.sumera.argallery.ui.feature.filter.contract.SetYearRangeReducer
import com.sumera.koreactor.behaviour.messages
import com.sumera.koreactor.behaviour.single
import com.sumera.koreactor.behaviour.triggers
import com.sumera.koreactor.reactor.MviReactor
import com.sumera.koreactor.reactor.data.MviAction
import io.reactivex.Observable
import javax.inject.Inject

class FilterReactor @Inject constructor(
        private val getCurrentFilterSingler: GetCurrentFilterSingler,
        private val setCurrentFilterCompletabler: SetCurrentFilterCompletabler
) : MviReactor<FilterState>() {

    override fun createInitialState(): FilterState {
        return FilterState(currentFilter = Filter.createDefault())
    }

    override fun bind(actions: Observable<MviAction<FilterState>>) {
        val onBackButtonClicked = actions.ofActionType<OnBackButtonClicked>()
        val onCloseButtonClicked = actions.ofActionType<OnCloseButtonClicked>()
        val onCloseAreaClicked = actions.ofActionType<OnCloseAreaClicked>()
        val onResetButtonClicked = actions.ofActionType<OnResetButtonClicked>()
        val onYearRangeChanged = actions.ofActionType<OnYearRangeChanged>()
        val onPriceRangeChanged = actions.ofActionType<OnPriceRangeChanged>()
        val onFirstCategoryStateChanged = actions.ofActionType<OnFirstCategoryStateChanged>()
        val onSecondCategoryStateChanged = actions.ofActionType<OnSecondCategoryStateChanged>()
        val onThirdCategoryStateChanged = actions.ofActionType<OnThirdCategoryStateChanged>()

        // Save current filter and close view
        Observable.merge(onBackButtonClicked, onCloseButtonClicked, onCloseAreaClicked)
                .flatMapSingle { stateSingle }
                .flatMapSingle { setCurrentFilterCompletabler.init(it.currentFilter).execute().toSingle { Unit } }
                .map { CloseEvent }
                .bindToView()

        // Get current filter when screen is initialized
        ObserveBehaviour<Any, Filter, FilterState>(
                triggers = triggers(attachLifecycleObservable),
                worker = single { getCurrentFilterSingler.execute() },
                message = messages { SetCurrentFilter(it) }
        ).bindToView()

        onResetButtonClicked
                .map { SetCurrentFilter(Filter.createDefault()) }
                .bindToView()

        onYearRangeChanged
                .map { SetYearRangeReducer(minYear = it.minYear, maxYear = it.maxYear) }
                .bindToView()

        onPriceRangeChanged
                .map { SetPriceRangeReducer(minPrice = it.minPrice, maxPrice = it.maxPrice) }
                .bindToView()

        onFirstCategoryStateChanged
                .map { SetFirstCategoryState(it.isEnabled) }
                .bindToView()

        onSecondCategoryStateChanged
                .map { SetSecondCategoryState(it.isEnabled) }
                .bindToView()

        onThirdCategoryStateChanged
                .map { SetThirdCategoryState(it.isEnabled) }
                .bindToView()
    }
}