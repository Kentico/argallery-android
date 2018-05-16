package com.sumera.argallery.reactors

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import com.sumera.argallery.data.store.ui.model.Picture
import com.sumera.argallery.domain.datasource.GetCurrentDataSourceData
import com.sumera.argallery.domain.focusedpicture.SetFocusedPictureCompletabler
import com.sumera.argallery.ui.feature.picturedetails.PictureDetailsReactor
import com.sumera.argallery.ui.feature.picturedetails.contract.OnPictureChanged
import com.sumera.argallery.ui.feature.picturedetails.contract.PictureDetailsState
import com.sumera.argallery.ui.feature.picturedetails.contract.ScrollToIndexEvent
import com.sumera.koreactor.reactor.MviReactor
import com.sumera.koreactorexampleapp.lib.ReactorTestRule
import com.sumera.koreactorexampleapp.lib.annotation.InitialLifecycleState
import com.sumera.koreactorexampleapp.lib.annotation.RunAfter
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject
import org.junit.Rule
import org.junit.Test

class PictureDetailsReactorTest {

    var mockGetCurrentDataSourceData = mock<GetCurrentDataSourceData>()
    var mockSetFocusedPictureCompletabler = mock<SetFocusedPictureCompletabler>()
    var mockInitialPicture = createPicture(id = "20")

    lateinit var getCurrentDataSourceDataSubject: PublishSubject<List<Picture>>
    lateinit var setFocusedPictureSubject: PublishSubject<Unit>

    @Rule
    @JvmField
    val reactorTest = object : ReactorTestRule<PictureDetailsState>() {
        override fun createNewReactorInstance(): MviReactor<PictureDetailsState> {
            whenever(mockGetCurrentDataSourceData.execute()).thenAnswer {
                getCurrentDataSourceDataSubject = PublishSubject.create<List<Picture>>()
                return@thenAnswer getCurrentDataSourceDataSubject
            }

            mockSetFocusedPictureCompletabler = mock<SetFocusedPictureCompletabler>()
            whenever(mockSetFocusedPictureCompletabler.execute()).thenAnswer {
                setFocusedPictureSubject = PublishSubject.create<Unit>()
                return@thenAnswer Single.fromObservable(setFocusedPictureSubject.take(1)).toCompletable()
            }

            whenever(mockSetFocusedPictureCompletabler.init(any())).thenReturn(mockSetFocusedPictureCompletabler)

            return PictureDetailsReactor(
                    mockGetCurrentDataSourceData,
                    mockSetFocusedPictureCompletabler,
                    mockInitialPicture
            )
        }
    }

    @Test
    @RunAfter(InitialLifecycleState.ON_RESUME)
    fun `check default state`() {
        reactorTest.runTest {
            assertNextState { PictureDetailsState(pictures = listOf(), initialPicture = mockInitialPicture) }
        }
    }

    @Test
    @RunAfter(InitialLifecycleState.ON_RESUME)
    fun `when pictures are fetched then they are set to state and scroll to initial picture event is send`() {
        reactorTest.runTest {
            val expectedList = listOf(createPicture(id = "10"), createPicture(id = "20"), createPicture(id = "30"))

            trigger { getCurrentDataSourceDataSubject.onNext(expectedList) }

            assertLastState { PictureDetailsState(pictures = expectedList, initialPicture = mockInitialPicture) }
            assertNextEvent { ScrollToIndexEvent(1) }
        }
    }

    @Test
    @RunAfter(InitialLifecycleState.ON_RESUME)
    fun `when pictures are scrolled then new focused picture is set`() {
        reactorTest.runTest {
            val expectedList = listOf(createPicture(id = "10"), createPicture(id = "20"), createPicture(id = "30"))
            trigger { getCurrentDataSourceDataSubject.onNext(expectedList) }

            sendAction { OnPictureChanged(2) }
            sendAction { OnPictureChanged(2) }

            trigger { setFocusedPictureSubject.onNext(Unit) }

            verify(mockSetFocusedPictureCompletabler).init(expectedList[2])
            verify(mockSetFocusedPictureCompletabler).execute()
        }
    }

    private fun createPicture(
            id: String = "id",
            title: String = "title",
            author: String = "author",
            description: String = "descr",
            imageUrl: String = "url",
            price: Int = 100
    ): Picture {
        return Picture(
                id = id,
                title = title,
                author = author,
                description = description,
                imageUrl = imageUrl,
                price = price
        )
    }

}