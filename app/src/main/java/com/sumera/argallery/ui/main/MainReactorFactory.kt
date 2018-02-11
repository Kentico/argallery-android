package com.sumera.argallery.ui.main

import com.sumera.koreactor.reactor.MviReactorFactory
import javax.inject.Inject
import javax.inject.Provider

class MainReactorFactory @Inject constructor(
        private val reactorProvider: Provider<MainReactor>
) : MviReactorFactory<MainReactor>() {

    override val reactor: MainReactor
        get() = reactorProvider.get()
}