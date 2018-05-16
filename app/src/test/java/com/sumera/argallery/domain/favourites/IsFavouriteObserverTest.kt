package com.sumera.argallery.domain.favourites

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import com.sumera.argallery.data.store.persistence.AppDatabase
import com.sumera.argallery.data.store.persistence.FavouritePicturesDao
import io.github.plastix.rxschedulerrule.RxSchedulerRule
import io.reactivex.Flowable
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class IsFavouriteObserverTest {

    @Rule
    @JvmField var rxRule: TestRule = RxSchedulerRule()

    var mockAppDatabase = mock<AppDatabase>()
    var mockFavouritePicturesDato = mock<FavouritePicturesDao>()

    lateinit var isFavouritesObserver: IsFavouriteObserver

    @Before
    fun setUp() {
        isFavouritesObserver = IsFavouriteObserver(mockAppDatabase)
        whenever(mockAppDatabase.favouritePicturesDao()).thenReturn(mockFavouritePicturesDato)
    }

    @Test
    fun `execute with unfavourited item should return false`() {
        // GIVEN
        whenever(mockFavouritePicturesDato.getItemsCountForId("1")).thenReturn(Flowable.just(0))

        // WHEN
        val result = isFavouritesObserver.init("1").execute().test()

        // THEN
        result.assertResult(false)
    }

    @Test
    fun `execute with favourited item should return true`() {
        // GIVEN
        whenever(mockFavouritePicturesDato.getItemsCountForId("1")).thenReturn(Flowable.just(1))

        // WHEN
        val result = isFavouritesObserver.init("1").execute().test()

        // THEN
        result.assertResult(true)
    }
}