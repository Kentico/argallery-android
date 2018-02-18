package com.sumera.argallery.domain.focusedpicture

import com.sumera.argallery.data.store.ui.focusedpicture.FocusedPictureStore
import com.sumera.argallery.data.store.ui.model.Picture
import com.sumera.argallery.domain.base.BaseCompletabler
import io.reactivex.Completable
import javax.inject.Inject

class SetFocusedPictureCompletabler @Inject constructor(
        private val focusedPictureStore: FocusedPictureStore
): BaseCompletabler() {

    private lateinit var focusedPicture: Picture

    fun init(focusedPicture: Picture): SetFocusedPictureCompletabler {
        this.focusedPicture = focusedPicture
        return this
    }

    override fun create(): Completable {
        return Completable.fromCallable {
            focusedPictureStore.setFocusedPicture(focusedPicture)
        }
    }
}