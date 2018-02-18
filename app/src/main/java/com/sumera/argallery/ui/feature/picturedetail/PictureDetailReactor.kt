package com.sumera.argallery.ui.feature.picturedetail

import com.sumera.argallery.data.store.ui.model.Picture
import com.sumera.argallery.ui.feature.picturedetail.contract.PictureDetailState
import com.sumera.koreactor.reactor.MviReactor
import com.sumera.koreactor.reactor.data.MviAction
import io.reactivex.Observable
import javax.inject.Inject

class PictureDetailReactor @Inject constructor(
        private val picture: Picture
) : MviReactor<PictureDetailState>() {

    override fun createInitialState(): PictureDetailState {
       return PictureDetailState(picture = picture)
    }

    override fun bind(actions: Observable<MviAction<PictureDetailState>>) {
        // Empty
    }
}