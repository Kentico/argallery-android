package com.sumera.argallery.data.store.ui.datasource

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import com.sumera.argallery.data.store.persistence.FavouritePicturesDataSourceStore
import com.sumera.argallery.data.store.remote.AllPicturesDataSourceStore
import com.sumera.argallery.data.store.remote.FilteredPicturesDataSourceStore
import com.sumera.argallery.data.store.ui.datasource.model.DataSourceType
import com.sumera.argallery.data.store.ui.model.PicturesWithLoadingState
import org.junit.Before
import org.junit.Test

class CurrentDataSourceStoreTest {

    var mockAllPicturesDataSourceStore = mock<AllPicturesDataSourceStore>()
    var mockFavouritePicturesDataSourceStore = mock<FavouritePicturesDataSourceStore>()
    var mockFilteredPicturesDataSourceStore = mock<FilteredPicturesDataSourceStore>()
    var mockPicturesWithLoadingState1 = mock<PicturesWithLoadingState>()
    var mockPicturesWithLoadingState2 = mock<PicturesWithLoadingState>()

    lateinit var currentDataSourceStore: CurrentDataSourceStore

    @Before
    fun setUp() {
        currentDataSourceStore = CurrentDataSourceStore(mockAllPicturesDataSourceStore, mockFavouritePicturesDataSourceStore, mockFilteredPicturesDataSourceStore)
    }

    @Test
    fun `check default data source type`() {
        // WHEN
        whenever(mockAllPicturesDataSourceStore.dataSourceType).thenReturn(DataSourceType.ALL)
        val result = currentDataSourceStore.getCurrentDataSourceTypeObservable().test()

        // THEN
        result.assertValue(DataSourceType.ALL)
    }

    @Test
    fun `change data source to filtered should change data source`() {
        // GIVEN
        whenever(mockFilteredPicturesDataSourceStore.dataSourceType).thenReturn(DataSourceType.FILTERED)
        whenever(mockAllPicturesDataSourceStore.dataSourceType).thenReturn(DataSourceType.ALL)
        val result = currentDataSourceStore.getCurrentDataSourceTypeObservable().test()

        // WHEN
        currentDataSourceStore.changeDataSource(DataSourceType.FILTERED)

        // THEN
        result.assertValues(DataSourceType.ALL, DataSourceType.FILTERED)
    }

    @Test
    fun `load more should call load more on current data source`() {
        // WHEN
        val result = currentDataSourceStore.loadMore().test()

        // THEN
        verify(mockAllPicturesDataSourceStore).loadMore()
        result.assertComplete()
    }
}