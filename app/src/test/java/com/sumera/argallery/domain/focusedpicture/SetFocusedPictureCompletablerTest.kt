package com.sumera.argallery.domain.focusedpicture

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.sumera.argallery.data.store.ui.focusedpicture.FocusedPictureStore
import com.sumera.argallery.data.store.ui.model.Picture
import io.github.plastix.rxschedulerrule.RxSchedulerRule
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class SetFocusedPictureCompletablerTest {

    @Rule
    @JvmField var rxRule: TestRule = RxSchedulerRule()

    var mockFocusedPictureStore = mock<FocusedPictureStore>()
    var mockPicture = mock<Picture>()

    lateinit var setFocusedPictureCompletabler: SetFocusedPictureCompletabler

    @Before
    fun setUp() {
        setFocusedPictureCompletabler = SetFocusedPictureCompletabler(mockFocusedPictureStore)
    }

    @Test
    fun `execute set focused picture to store should add pictures to store`() {
        // WHEN
        val result = setFocusedPictureCompletabler.init(mockPicture).execute().test()

        // THEN
        verify(mockFocusedPictureStore).setFocusedPicture(mockPicture)
        result.assertComplete()
    }
}