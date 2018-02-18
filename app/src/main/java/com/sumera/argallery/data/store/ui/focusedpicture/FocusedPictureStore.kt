package com.sumera.argallery.data.store.ui.focusedpicture

import com.sumera.argallery.data.store.ui.model.Picture
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FocusedPictureStore @Inject constructor() {

    private val focusedPictureSubject = BehaviorSubject.create<Picture>()

    fun setFocusedPicture(picture: Picture) {
        focusedPictureSubject.onNext(picture)
    }

    fun getFocusedPictureObservable(): Observable<Picture> {
        return focusedPictureSubject.hide()
    }
}