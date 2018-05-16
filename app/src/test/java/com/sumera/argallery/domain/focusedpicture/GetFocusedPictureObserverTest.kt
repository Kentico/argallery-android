package com.sumera.argallery.domain.focusedpicture

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import com.sumera.argallery.data.store.ui.focusedpicture.FocusedPictureStore
import com.sumera.argallery.data.store.ui.model.Picture
import io.github.plastix.rxschedulerrule.RxSchedulerRule
import io.reactivex.Observable
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class GetFocusedPictureObserverTest {

    @Rule
    @JvmField var rxRule: TestRule = RxSchedulerRule()

    var mockFocusedPictureStore = mock<FocusedPictureStore>()
    var mockPicture1 = mock<Picture>()
    var mockPicture2 = mock<Picture>()

    lateinit var getFocusedPictureObserver: GetFocusedPictureObserver

    @Before
    fun setUp() {
        getFocusedPictureObserver = GetFocusedPictureObserver(mockFocusedPictureStore)
    }

    @Test
    fun `execute get focused picture to store shoult return pictures`() {
        // GIVEN
        whenever(mockFocusedPictureStore.getFocusedPictureObservable()).thenReturn(Observable.just(mockPicture1, mockPicture2))

        // WHEN
        val result = getFocusedPictureObserver.execute().test()

        // THEN
        result.assertResult(mockPicture1, mockPicture2)
    }
}