package com.sumera.argallery.ui.feature.picturelist

import com.sumera.argallery.data.store.ui.model.PicturesWithLoadingState
import com.sumera.argallery.domain.datasource.GetCurrentDataSourceStateObserver
import com.sumera.argallery.tools.behaviours.ObserveBehaviour
import com.sumera.argallery.ui.base.BaseReactor
import com.sumera.argallery.ui.feature.main.contract.PictureListState
import com.sumera.argallery.ui.feature.picturelist.contract.ShowLoadingStateWithData
import com.sumera.koreactor.behaviour.messages
import com.sumera.koreactor.behaviour.observable
import com.sumera.koreactor.behaviour.triggers
import com.sumera.koreactor.reactor.data.MviAction
import io.reactivex.Observable
import javax.inject.Inject

class PictureListReactor @Inject constructor(
        private val getCurrentDataSourceStateInteractor: GetCurrentDataSourceStateObserver
) : BaseReactor<PictureListState>() {

    override fun createInitialState() = PictureListState(
            isLoading = false,
            isError = false,
            pictures = listOf(),
            isLoadingMoreEnabled = false,
            focusedPicture = null
    )

    override fun bind(actions: Observable<MviAction<PictureListState>>) {
        ObserveBehaviour<Any, PicturesWithLoadingState, PictureListState>(
                triggers = triggers(attachLifecycleObservable),
                worker = observable { getCurrentDataSourceStateInteractor.execute() },
                message = messages { ShowLoadingStateWithData(it) }
        ).bindToView()
    }
}