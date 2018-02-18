package com.sumera.argallery.ui.feature.picturelist

import com.sumera.argallery.data.store.ui.model.Picture
import com.sumera.argallery.data.store.ui.model.PicturesWithLoadingState
import com.sumera.argallery.domain.datasource.GetCurrentDataSourceStateObserver
import com.sumera.argallery.domain.focusedpicture.GetFocusedPictureObserver
import com.sumera.argallery.tools.koreactor.ObserveBehaviour
import com.sumera.argallery.ui.base.BaseReactor
import com.sumera.argallery.ui.feature.main.contract.PictureListState
import com.sumera.argallery.ui.feature.picturelist.contract.NavigateToPictureDetails
import com.sumera.argallery.ui.feature.picturelist.contract.OnFilterClicked
import com.sumera.argallery.ui.feature.picturelist.contract.OnFocusedItemChanged
import com.sumera.argallery.ui.feature.picturelist.contract.OnPictureClicked
import com.sumera.argallery.ui.feature.picturelist.contract.SetFocusedPicture
import com.sumera.argallery.ui.feature.picturelist.contract.SetIsScrollToFocusedItemEnabled
import com.sumera.argallery.ui.feature.picturelist.contract.ShowLoadingStateWithData
import com.sumera.koreactor.behaviour.messages
import com.sumera.koreactor.behaviour.observable
import com.sumera.koreactor.behaviour.single
import com.sumera.koreactor.behaviour.triggers
import com.sumera.koreactor.reactor.data.MviAction
import io.reactivex.Observable
import javax.inject.Inject

class PictureListReactor @Inject constructor(
        private val getCurrentDataSourceStateInteractor: GetCurrentDataSourceStateObserver,
        private val getFocusedPictureObserver: GetFocusedPictureObserver
) : BaseReactor<PictureListState>() {

    override fun createInitialState() = PictureListState(
            isLoading = false,
            isError = false,
            pictures = listOf(),
            isLoadingMoreEnabled = false,
            isScrollToFocusedItemEnabled = true,
            focusedPicture = null
    )

    override fun bind(actions: Observable<MviAction<PictureListState>>) {
        val onFocusedItemChanged = actions.ofActionType<OnFocusedItemChanged>()
        val onPictureClicked = actions.ofActionType<OnPictureClicked>()
        val onFilterClicked = actions.ofActionType<OnFilterClicked>()

        // Observe pictures with loading state from global state
        ObserveBehaviour<Any, PicturesWithLoadingState, PictureListState>(
                triggers = triggers(attachLifecycleObservable),
                worker = observable { getCurrentDataSourceStateInteractor.execute() },
                message = messages { ShowLoadingStateWithData(it) }
        ).bindToView()

        // Observe focused picture from global state
        ObserveBehaviour<Any, Picture, PictureListState>(
                triggers = triggers(attachLifecycleObservable),
                worker = observable { getFocusedPictureObserver.execute() },
                message = messages { SetFocusedPicture(it) }
        ).bindToView()

        // Observe focused picture from user scroll actions
        ObserveBehaviour<Int, Picture, PictureListState>(
                triggers = triggers(onFocusedItemChanged.map { it.itemIndex }),
                worker = single { index -> stateSingle.map { it.pictures[index] }},
                message = messages { SetFocusedPicture(it) }
        ).bindToView()

        // Enable automatic scroll to picture item
        startLifecycleObservable
                .map { SetIsScrollToFocusedItemEnabled(false) }
                .bindToView()

        // Disable automatic scroll to picture item
        stopLifecycleObservable
                .map { SetIsScrollToFocusedItemEnabled(true) }
                .bindToView()

        onPictureClicked
                .map { NavigateToPictureDetails }
                .bindToView()

    }
}