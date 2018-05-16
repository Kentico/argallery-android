package com.sumera.argallery.domain.filter

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import com.sumera.argallery.data.store.ui.FilterStore
import com.sumera.argallery.data.store.ui.model.Filter
import io.github.plastix.rxschedulerrule.RxSchedulerRule
import io.reactivex.Single
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class GetCurrentFilterSinglerTest {

    @Rule
    @JvmField var rxRule: TestRule = RxSchedulerRule()

    var mockFilterStore = mock<FilterStore>()
    var mockFilter = mock<Filter>()

    lateinit var mockGetCurrentFilterSingler: GetCurrentFilterSingler

    @Before
    fun setUp() {
        mockGetCurrentFilterSingler = GetCurrentFilterSingler(mockFilterStore)
    }

    @Test
    fun `execute should return current filter`() {
        // GIVEN
        whenever(mockFilterStore.getCurrentFilterSingle()).thenReturn(Single.just(mockFilter))

        // WHEN
        val result = mockGetCurrentFilterSingler.execute().test()

        // THEN
        result.assertValues(mockFilter)
    }
}