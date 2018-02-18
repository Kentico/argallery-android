package com.sumera.argallery.ui.feature.picturedetail

import com.sumera.koreactor.reactor.MviReactorFactory
import javax.inject.Inject
import javax.inject.Provider

class PictureDetailReactorFactory @Inject constructor(
        private val reactorProvider: Provider<PictureDetailReactor>
) : MviReactorFactory<PictureDetailReactor>() {

    override val reactor: PictureDetailReactor
        get() = reactorProvider.get()
}