package com.sumera.argallery.domain.filter

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.sumera.argallery.data.store.ui.FilterStore
import com.sumera.argallery.data.store.ui.model.Filter
import io.github.plastix.rxschedulerrule.RxSchedulerRule
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class SetCurrentFilterCompletablerTest {

    @Rule
    @JvmField var rxRule: TestRule = RxSchedulerRule()

    var mockFilterStore = mock<FilterStore>()
    var mockFilter = mock<Filter>()

    lateinit var setCurrentFilterCompletabler: SetCurrentFilterCompletabler

    @Before
    fun setUp() {
        setCurrentFilterCompletabler = SetCurrentFilterCompletabler(mockFilterStore)
    }

    @Test
    fun `execute should set filter to store`() {
        // WHEN
        val result = setCurrentFilterCompletabler.init(mockFilter).execute().test()

        // THEN
        verify(mockFilterStore).setCurrentFilter(mockFilter)
        result.assertComplete()
    }
}