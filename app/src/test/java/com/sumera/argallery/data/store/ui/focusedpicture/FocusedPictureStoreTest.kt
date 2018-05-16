package com.sumera.argallery.data.store.ui.focusedpicture

import com.nhaarman.mockito_kotlin.mock
import com.sumera.argallery.data.store.ui.model.Picture
import org.junit.Before
import org.junit.Test

class FocusedPictureStoreTest {

    var mockPicture1 = mock<Picture>()
    var mockPicture2 = mock<Picture>()

    lateinit var focusedPictureStore: FocusedPictureStore

    @Before
    fun setUp() {
        focusedPictureStore = FocusedPictureStore()
    }

    @Test
    fun `set picture change focused picture`() {
        // GIVEN
        val result = focusedPictureStore.getFocusedPictureObservable().test()

        // WHEN
        focusedPictureStore.setFocusedPicture(mockPicture1)

        // THEN
        result.assertValue(mockPicture1)
    }

    @Test
    fun `multiple sets change focused pictures multiple times`() {
        // GIVEN
        val result = focusedPictureStore.getFocusedPictureObservable().test()

        // WHEN
        focusedPictureStore.setFocusedPicture(mockPicture1)
        focusedPictureStore.setFocusedPicture(mockPicture2)

        // THEN
        result.assertValues(mockPicture1, mockPicture2)
    }
}