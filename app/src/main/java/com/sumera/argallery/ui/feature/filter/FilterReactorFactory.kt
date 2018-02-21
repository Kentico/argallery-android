package com.sumera.argallery.ui.feature.filter

import com.sumera.koreactor.reactor.MviReactorFactory
import javax.inject.Inject
import javax.inject.Provider

class FilterReactorFactory @Inject constructor(
        private val reactorProvider: Provider<FilterReactor>
) : MviReactorFactory<FilterReactor>() {

    override val reactor: FilterReactor
        get() = reactorProvider.get()
}