package com.sumera.argallery.domain.favourites

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import com.sumera.argallery.data.mapper.PictureMapper
import com.sumera.argallery.data.store.persistence.AppDatabase
import com.sumera.argallery.data.store.persistence.FavouritePicturesDao
import com.sumera.argallery.data.store.persistence.model.PersistedPicture
import com.sumera.argallery.data.store.ui.model.Picture
import io.github.plastix.rxschedulerrule.RxSchedulerRule
import io.reactivex.Flowable
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class GetAllFavouritesObserverTest {

    @Rule
    @JvmField var rxRule: TestRule = RxSchedulerRule()

    var mockAppDatabase = mock<AppDatabase>()
    var mockMapper = mock<PictureMapper>()
    var mockFavouritePicturesDato = mock<FavouritePicturesDao>()
    var mockPicture = mock<Picture>()
    var mockPersistencePicture = mock<PersistedPicture>()

    lateinit var getAllFavouritesObserver: GetAllFavouritesObserver

    @Before
    fun setUp() {
        getAllFavouritesObserver = GetAllFavouritesObserver(mockAppDatabase, mockMapper)
        whenever(mockAppDatabase.favouritePicturesDao()).thenReturn(mockFavouritePicturesDato)
    }

    @Test
    fun `execute should return picture from dao`() {
        // GIVEN
        whenever(mockFavouritePicturesDato.getAll()).thenReturn(Flowable.just(listOf(mockPersistencePicture)))
        whenever(mockMapper.toPictures(listOf(mockPersistencePicture))).thenReturn(listOf(mockPicture))

        // WHEN
        val result = getAllFavouritesObserver.execute().test()

        // THEN
        result.assertResult(listOf(mockPicture))
    }
}