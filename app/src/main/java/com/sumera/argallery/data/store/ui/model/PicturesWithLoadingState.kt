package com.sumera.argallery.data.store.ui.model

import com.sumera.argallery.data.store.datasource.model.LoadingState

data class PicturesWithLoadingState(
        val pictures: List<Picture>,
        val loadingState: LoadingState
) {

    companion object {
        fun createDefault(): PicturesWithLoadingState {
            return PicturesWithLoadingState(pictures = listOf(), loadingState = LoadingState.INACTIVE)
        }
    }
}

