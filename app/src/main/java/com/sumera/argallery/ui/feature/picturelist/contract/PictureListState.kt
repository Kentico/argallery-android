package com.sumera.argallery.ui.feature.picturelist.contract

import com.sumera.argallery.data.store.ui.datasource.model.DataSourceType
import com.sumera.argallery.data.store.ui.model.Picture
import com.sumera.koreactor.reactor.data.MviState

data class PictureListState(
        val isLoading: Boolean,
        val isError: Boolean,
        val pictures: List<Picture>,
        val isLoadingMoreEnabled: Boolean,
        val isScrollToGloballySelectedPictureEnabled: Boolean,
        val dataSourceType: DataSourceType,
        val locallySelectedPicture: Picture?,
        val globallySelectedPicture: Picture?
) : MviState

fun PictureListState.hasEmptyState(): Boolean {
    return isError.not() && isLoading.not() && pictures.isEmpty()
}