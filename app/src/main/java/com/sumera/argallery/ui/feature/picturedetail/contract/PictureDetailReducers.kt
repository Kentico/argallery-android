package com.sumera.argallery.ui.feature.picturedetail.contract

import com.sumera.koreactor.reactor.data.MviStateReducer

sealed class PictureDetailReducers : MviStateReducer<PictureDetailState>

data class SetFavourite(val isFavourite: Boolean) : PictureDetailReducers() {
    override fun reduce(oldState: PictureDetailState): PictureDetailState {
        return oldState.copy(isFavourite = isFavourite)
    }
}