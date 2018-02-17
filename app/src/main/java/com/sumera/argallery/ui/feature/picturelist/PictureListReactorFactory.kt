package com.sumera.argallery.ui.feature.picturelist

import com.sumera.koreactor.reactor.MviReactorFactory
import javax.inject.Inject
import javax.inject.Provider

class PictureListReactorFactory @Inject constructor(
        private val reactorProvider: Provider<PictureListReactor>
) : MviReactorFactory<PictureListReactor>() {

    override val reactor: PictureListReactor
        get() = reactorProvider.get()
}