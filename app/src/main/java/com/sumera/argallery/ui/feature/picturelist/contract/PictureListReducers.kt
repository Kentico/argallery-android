package com.sumera.argallery.ui.feature.picturelist.contract

import com.sumera.argallery.data.store.ui.datasource.model.LoadingState
import com.sumera.argallery.data.store.ui.model.Picture
import com.sumera.argallery.data.store.ui.model.PicturesWithLoadingState
import com.sumera.argallery.ui.feature.main.contract.PictureListState
import com.sumera.koreactor.reactor.data.MviStateReducer

sealed class PictureListReducers : MviStateReducer<PictureListState>

data class ShowLoadingStateWithData(
        private val picturesWithLoadingState: PicturesWithLoadingState
) : PictureListReducers() {
    override fun reduce(oldState: PictureListState): PictureListState {
        val data = picturesWithLoadingState.pictures
        val loadingState = picturesWithLoadingState.loadingState
        return oldState.copy(
                isLoading = loadingState == LoadingState.LOADING,
                isError = loadingState == LoadingState.ERROR,
                isLoadingMoreEnabled = loadingState == LoadingState.INACTIVE,
                pictures = data
        )
    }
}

data class SetFocusedPicture(
        private val focusedPicture: Picture
) : PictureListReducers() {
    override fun reduce(oldState: PictureListState): PictureListState {
        return oldState.copy(focusedPicture = focusedPicture)
    }
}

data class SetIsScrollToFocusedItemEnabled(
        private val isEnabled: Boolean
) : PictureListReducers() {
    override fun reduce(oldState: PictureListState): PictureListState {
        return oldState.copy(isScrollToFocusedItemEnabled = isEnabled)
    }
}
