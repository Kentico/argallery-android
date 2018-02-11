package com.sumera.argallery.ui.main

import com.sumera.argallery.data.remote.model.ContentItems
import com.sumera.argallery.domain.remote.GetPictureInteractor
import com.sumera.argallery.ui.base.BaseReactor
import com.sumera.argallery.ui.main.contract.MainState
import com.sumera.koreactor.behaviour.implementation.LoadingBehaviour
import com.sumera.koreactor.behaviour.messages
import com.sumera.koreactor.behaviour.single
import com.sumera.koreactor.behaviour.triggers
import com.sumera.koreactor.reactor.data.MviAction
import io.reactivex.Observable
import javax.inject.Inject

class MainReactor @Inject constructor(
        private val getPictureInteractor: GetPictureInteractor
) : BaseReactor<MainState>() {

    override fun createInitialState() = MainState

    override fun bind(actions: Observable<MviAction<MainState>>) {
        LoadingBehaviour<Any, ContentItems, MainState>(
                triggers = triggers(attachLifecycleObservable),
                loadWorker = single { getPictureInteractor.execute() },
                cancelPrevious = true,
                dataMessage = messages(),
                loadingMessage = messages(),
                errorMessage = messages()
        ).bindToView()
    }
}