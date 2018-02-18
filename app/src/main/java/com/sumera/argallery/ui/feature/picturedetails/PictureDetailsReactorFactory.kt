package com.sumera.argallery.ui.feature.picturedetails

import com.sumera.koreactor.reactor.MviReactorFactory
import javax.inject.Inject
import javax.inject.Provider

class PictureDetailsReactorFactory @Inject constructor(
        private val reactorProvider: Provider<PictureDetailsReactor>
) : MviReactorFactory<PictureDetailsReactor>() {

    override val reactor: PictureDetailsReactor
        get() = reactorProvider.get()
}