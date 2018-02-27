package com.sumera.argallery.ui.feature.picturelist

import com.sumera.argallery.data.store.ui.datasource.model.DataSourceType
import com.sumera.argallery.data.store.ui.model.Picture
import com.sumera.argallery.data.store.ui.model.PicturesWithLoadingState
import com.sumera.argallery.domain.datasource.GetCurrentDataSourceStateObserver
import com.sumera.argallery.domain.datasource.GetCurrentDataSourceTypeObserver
import com.sumera.argallery.domain.datasource.LoadMoreFromDataSourceCompletabler
import com.sumera.argallery.domain.datasource.SetCurrentDataSourceCompletabler
import com.sumera.argallery.domain.focusedpicture.GetFocusedPictureObserver
import com.sumera.argallery.tools.DEFAULT_DATA_SOURCE
import com.sumera.argallery.tools.extensions.isInBounds
import com.sumera.argallery.tools.koreactor.ExecuteBehaviour
import com.sumera.argallery.tools.koreactor.ObserveBehaviour
import com.sumera.argallery.ui.base.BaseReactor
import com.sumera.argallery.ui.feature.picturelist.contract.NavigateToFilter
import com.sumera.argallery.ui.feature.picturelist.contract.NavigateToPictureDetails
import com.sumera.argallery.ui.feature.picturelist.contract.OnChangeFilterClicked
import com.sumera.argallery.ui.feature.picturelist.contract.OnFocusedIndexChanged
import com.sumera.argallery.ui.feature.picturelist.contract.OnListEndReached
import com.sumera.argallery.ui.feature.picturelist.contract.OnPictureClicked
import com.sumera.argallery.ui.feature.picturelist.contract.OnShowAllClicked
import com.sumera.argallery.ui.feature.picturelist.contract.OnTryAgainClicked
import com.sumera.argallery.ui.feature.picturelist.contract.PictureListState
import com.sumera.argallery.ui.feature.picturelist.contract.SetDataSourceType
import com.sumera.argallery.ui.feature.picturelist.contract.SetGloballySelectedPicture
import com.sumera.argallery.ui.feature.picturelist.contract.SetIsScrollToFocusedItemEnabled
import com.sumera.argallery.ui.feature.picturelist.contract.SetLocallySelectedPicture
import com.sumera.argallery.ui.feature.picturelist.contract.ShowLoadingStateWithData
import com.sumera.koreactor.behaviour.completable
import com.sumera.koreactor.behaviour.messages
import com.sumera.koreactor.behaviour.observable
import com.sumera.koreactor.behaviour.triggers
import com.sumera.koreactor.reactor.data.MviAction
import io.reactivex.Maybe
import io.reactivex.Observable
import javax.inject.Inject

class PictureListReactor @Inject constructor(
        private val getCurrentDataSourceStateObserver: GetCurrentDataSourceStateObserver,
        private val getCurrentDataSourceTypeObserver: GetCurrentDataSourceTypeObserver,
        private val setCurrentDataSourceCompletabler: SetCurrentDataSourceCompletabler,
        private val loadMoreFromDataSourceCompletabler: LoadMoreFromDataSourceCompletabler,
        private val getFocusedPictureObserver: GetFocusedPictureObserver
) : BaseReactor<PictureListState>() {

    override fun createInitialState() = PictureListState(
            isLoading = false,
            isError = false,
            pictures = listOf(),
            isLoadingMoreEnabled = false,
            isScrollToGloballySelectedPictureEnabled = true,
            dataSourceType = DEFAULT_DATA_SOURCE,
            locallySelectedPicture = null,
            globallySelectedPicture = null
    )

    override fun bind(actions: Observable<MviAction<PictureListState>>) {
        val onFocusedIndexChanged = actions.ofActionType<OnFocusedIndexChanged>()
        val onPictureClicked = actions.ofActionType<OnPictureClicked>()
        val onListEndReached = actions.ofActionType<OnListEndReached>()
        val onTryAgainClicked = actions.ofActionType<OnTryAgainClicked>()
        val onChangeFilterClicked = actions.ofActionType<OnChangeFilterClicked>()
        val onShowAllClicked = actions.ofActionType<OnShowAllClicked>()

        val onLoadMoreDataAction = onListEndReached
                .flatMapSingle { stateSingle }
                .filter { it.isLoadingMoreEnabled }

        // Observe focused picture from user scroll actions
        onFocusedIndexChanged
                .flatMapMaybe { getPictureIfIsInBounds(it.itemIndex) }
                .map { SetLocallySelectedPicture(it) }
                .bindToView()

        // Observe current data source type
        ObserveBehaviour<Any, DataSourceType, PictureListState>(
                triggers = triggers(attachLifecycleObservable),
                worker = observable { getCurrentDataSourceTypeObserver.execute() },
                message = messages { SetDataSourceType(it) }
        ).bindToView()

        // Observe pictures with loading state from global state
        ObserveBehaviour<Any, PicturesWithLoadingState, PictureListState>(
                triggers = triggers(attachLifecycleObservable),
                worker = observable { getCurrentDataSourceStateObserver.execute() },
                message = messages { ShowLoadingStateWithData(it) }
        ).bindToView()

        // Observe focused picture from global state
        ObserveBehaviour<Any, Picture, PictureListState>(
                triggers = triggers(attachLifecycleObservable),
                worker = observable { getFocusedPictureObserver.execute() },
                message = messages { SetGloballySelectedPicture(it) }
        ).bindToView()

        // Load more data if user reached end of picture list or clicked on try again
        ExecuteBehaviour<Any, PictureListState>(
                triggers = triggers(onLoadMoreDataAction, onTryAgainClicked),
                worker = completable { loadMoreFromDataSourceCompletabler.execute() }
        ).bindToView()

        // Enable automatic scroll to picture item
        startLifecycleObservable
                .map { SetIsScrollToFocusedItemEnabled(false) }
                .bindToView()

        // Disable automatic scroll to picture item
        stopLifecycleObservable
                .map { SetIsScrollToFocusedItemEnabled(true) }
                .bindToView()

        // Set ALL as data source
        ExecuteBehaviour<Any, PictureListState>(
                triggers = triggers(onShowAllClicked),
                worker = completable { setCurrentDataSourceCompletabler.init(DataSourceType.ALL).execute() }
        ).bindToView()

        // Navigate to picture details
        onPictureClicked
                .map { NavigateToPictureDetails(it.picture) }
                .bindToView()

        // Navigate to filter
        onChangeFilterClicked
                .map { NavigateToFilter }
                .bindToView()
    }

    private fun getPictureIfIsInBounds(index: Int): Maybe<Picture> {
        return stateSingle
                .flatMapMaybe { when {
                        it.pictures.isInBounds(index) -> {
                            Maybe.just(it.pictures[index])
                        }
                        (it.isLoading || it.isError) && index == it.pictures.size && it.pictures.isNotEmpty()-> {
                            Maybe.just(it.pictures.last())
                        }
                        else -> {
                            Maybe.never()
                        }
                    }
                }
    }
}