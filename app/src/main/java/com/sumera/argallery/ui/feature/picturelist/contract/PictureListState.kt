package com.sumera.argallery.ui.feature.main.contract

import com.sumera.argallery.data.store.ui.model.Picture
import com.sumera.koreactor.reactor.data.MviState

data class PictureListState(
        val isLoading: Boolean,
        val isError: Boolean,
        val pictures: List<Picture>,
        val isLoadingMoreEnabled: Boolean,
        val isScrollToFocusedItemEnabled: Boolean,
        val focusedPicture: Picture?
) : MviState
