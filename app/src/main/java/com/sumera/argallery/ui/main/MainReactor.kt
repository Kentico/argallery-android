package com.sumera.argallery.ui.main

import com.sumera.argallery.ui.base.BaseReactor
import com.sumera.argallery.ui.main.contract.MainState
import com.sumera.koreactor.reactor.data.MviAction
import io.reactivex.Observable
import javax.inject.Inject

class MainReactor @Inject constructor() : BaseReactor<MainState>() {

    override fun createInitialState() = MainState

    override fun bind(actions: Observable<MviAction<MainState>>) {

    }
}