package com.sumera.argallery.data.store.ui

import com.nhaarman.mockito_kotlin.mock
import com.sumera.argallery.data.store.ui.model.Filter
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class FilterStoreTest {

    var mockFilter = mock<Filter>()

    lateinit var filterStore: FilterStore

    @Before
    fun setUp() {
        filterStore = FilterStore()
    }

    @Test
    fun `get current filter should return default value`() {
        assertEquals(filterStore.getCurrentFilter(), Filter.createDefault())
    }

    @Test
    fun `get current filter single should return default value`() {
        // WHEN
        val result = filterStore.getCurrentFilterSingle().test()

        // THEN
        result.assertValue(Filter.createDefault())
    }

    @Test
    fun `get current filter observable should return default value`() {
        // WHEN
        val result = filterStore.getCurrentFilterObservable().test()

        // THEN
        result.assertValue(Filter.createDefault())
    }

    @Test
    fun `set current filter should change filter and emit new one`() {
        // GIVEN
        val result = filterStore.getCurrentFilterObservable().test()

        // WHEN
        filterStore.setCurrentFilter(mockFilter)

        // THEN
        result.assertValues(Filter.createDefault(), mockFilter)
        assertEquals(filterStore.getCurrentFilter(), mockFilter)
    }

}