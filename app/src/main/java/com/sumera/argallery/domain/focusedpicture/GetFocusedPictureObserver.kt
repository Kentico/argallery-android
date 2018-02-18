package com.sumera.argallery.domain.focusedpicture

import com.sumera.argallery.data.store.ui.focusedpicture.FocusedPictureStore
import com.sumera.argallery.data.store.ui.model.Picture
import com.sumera.argallery.domain.base.BaseObserver
import io.reactivex.Observable
import javax.inject.Inject

class GetFocusedPictureObserver @Inject constructor(
        private val focusedPictureStore: FocusedPictureStore
) : BaseObserver<Picture>() {

    override fun create(): Observable<Picture> {
        return focusedPictureStore.getFocusedPictureObservable()
    }
}