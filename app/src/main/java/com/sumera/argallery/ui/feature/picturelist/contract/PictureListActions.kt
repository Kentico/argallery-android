package com.sumera.argallery.ui.feature.picturelist.contract

import com.sumera.argallery.data.store.ui.model.Picture
import com.sumera.argallery.ui.feature.main.contract.PictureListState
import com.sumera.koreactor.reactor.data.MviAction

sealed class PictureListActions : MviAction<PictureListState>

data class OnFocusedItemChanged(val itemIndex: Int) : PictureListActions()

data class OnPictureClicked(val picture: Picture) : PictureListActions()