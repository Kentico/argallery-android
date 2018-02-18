package com.sumera.argallery.ui.feature.picturedetails.contract

import com.sumera.argallery.data.store.ui.model.Picture
import com.sumera.koreactor.reactor.data.MviStateReducer

sealed class PictureDetailsReducers : MviStateReducer<PictureDetailsState>

data class SetPicturesDataReducer(val newPictures: List<Picture>) : PictureDetailsReducers() {
    override fun reduce(oldState: PictureDetailsState): PictureDetailsState {
        return oldState.copy(pictures = newPictures)
    }
}