package com.sumera.argallery.reactors

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.never
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import com.sumera.argallery.data.store.ui.datasource.model.DataSourceType
import com.sumera.argallery.data.store.ui.datasource.model.LoadingState
import com.sumera.argallery.data.store.ui.model.Picture
import com.sumera.argallery.data.store.ui.model.PicturesWithLoadingState
import com.sumera.argallery.domain.datasource.GetCurrentDataSourceStateObserver
import com.sumera.argallery.domain.datasource.GetCurrentDataSourceTypeObserver
import com.sumera.argallery.domain.datasource.LoadMoreFromDataSourceCompletabler
import com.sumera.argallery.domain.datasource.SetCurrentDataSourceCompletabler
import com.sumera.argallery.domain.focusedpicture.GetFocusedPictureObserver
import com.sumera.argallery.ui.feature.picturelist.PictureListReactor
import com.sumera.argallery.ui.feature.picturelist.contract.OnListEndReached
import com.sumera.argallery.ui.feature.picturelist.contract.PictureListState
import com.sumera.koreactor.reactor.MviReactor
import com.sumera.koreactorexampleapp.lib.ReactorTestRule
import com.sumera.koreactorexampleapp.lib.annotation.InitialLifecycleState
import com.sumera.koreactorexampleapp.lib.annotation.RunAfter
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class PictureListReactorTest {

    private val mockGetCurrentDataSourceStateObserver = mock<GetCurrentDataSourceStateObserver>()
    private val mockGetCurrentDataSourceTypeObserver = mock<GetCurrentDataSourceTypeObserver>()
    private val mockSetCurrentDataSourceCompletabler = mock<SetCurrentDataSourceCompletabler>()
    private val mockLoadMoreFromDataSourceCompletabler = mock<LoadMoreFromDataSourceCompletabler>()
    private val mockGetFocusedPictureObserver = mock<GetFocusedPictureObserver>()

    lateinit var getCurrentDataSourceStateSubject: PublishSubject<PicturesWithLoadingState>
    lateinit var getCurrentDataSourceTypeSubject: PublishSubject<DataSourceType>
    lateinit var setCurrentDataSourceSubject: PublishSubject<Unit>
    lateinit var loadMoreFromDataSourceSubject: PublishSubject<Unit>
    lateinit var getFocusedPictureSubject: PublishSubject<Picture>

    @Rule
    @JvmField
    val reactorTest = object : ReactorTestRule<PictureListState>() {
        override fun createNewReactorInstance(): MviReactor<PictureListState> {
            whenever(mockGetCurrentDataSourceStateObserver.execute()).thenAnswer {
                getCurrentDataSourceStateSubject = PublishSubject.create<PicturesWithLoadingState>()
                return@thenAnswer getCurrentDataSourceStateSubject
            }

            whenever(mockGetCurrentDataSourceTypeObserver.execute()).thenAnswer {
                getCurrentDataSourceTypeSubject = PublishSubject.create<DataSourceType>()
                return@thenAnswer getCurrentDataSourceTypeSubject
            }

            whenever(mockSetCurrentDataSourceCompletabler.execute()).thenAnswer {
                setCurrentDataSourceSubject = PublishSubject.create<Unit>()
                return@thenAnswer Single.fromObservable(setCurrentDataSourceSubject.take(1)).toCompletable()
            }

            whenever(mockLoadMoreFromDataSourceCompletabler.execute()).thenAnswer {
                loadMoreFromDataSourceSubject = PublishSubject.create<Unit>()
                return@thenAnswer Single.fromObservable(loadMoreFromDataSourceSubject.take(1)).toCompletable()
            }

            whenever(mockGetFocusedPictureObserver.execute()).thenAnswer {
                getFocusedPictureSubject = PublishSubject.create<Picture>()
                return@thenAnswer getFocusedPictureSubject
            }

            return PictureListReactor(
                    mockGetCurrentDataSourceStateObserver,
                    mockGetCurrentDataSourceTypeObserver,
                    mockSetCurrentDataSourceCompletabler,
                    mockLoadMoreFromDataSourceCompletabler,
                    mockGetFocusedPictureObserver
            )
        }
    }

    @Before
    fun setUp() {
        whenever(mockSetCurrentDataSourceCompletabler.init(any())).thenReturn(mockSetCurrentDataSourceCompletabler)
    }

    @Test
    @RunAfter(InitialLifecycleState.ON_RESUME)
    fun `check default state`() {
        reactorTest.runTest {
            assertNextState { createState() }
        }
    }

    @Test
    @RunAfter(InitialLifecycleState.ON_RESUME)
    fun `when get data source type emits new item the it should change the state`() {
        reactorTest.runTest {
            trigger { getCurrentDataSourceTypeSubject.onNext(DataSourceType.FILTERED) }

            assertLastState { createState(dataSourceType = DataSourceType.FILTERED, isScrollToGloballySelectedPictureEnabled = false) }
        }
    }

    @Test
    @RunAfter(InitialLifecycleState.ON_RESUME)
    fun `when get data source state emits new item the it should change the state`() {
        reactorTest.runTest {
            trigger {
                getCurrentDataSourceStateSubject.onNext(
                    createPictureLoadingState(LoadingState.LOADING, createPicture(id = "1"), createPicture(id = "2"))
                )
            }

            assertLastState {
                createState(
                        isLoading = true,
                        isLoadingMoreEnabled = false,
                        isScrollToGloballySelectedPictureEnabled = false,
                        pictures = listOf(createPicture(id = "1"), createPicture(id = "2"))
                )
            }
        }
    }

    @Test
    @RunAfter(InitialLifecycleState.ON_RESUME)
    fun `load more action when loading more is enabled should send call load more interactor`() {
        reactorTest.runTest {
            trigger {
                getCurrentDataSourceStateSubject.onNext(
                        createPictureLoadingState(LoadingState.INACTIVE, createPicture(id = "1"), createPicture(id = "2"))
                )
            }

            sendAction { OnListEndReached }

            verify(mockLoadMoreFromDataSourceCompletabler).execute()
        }
    }

    @Test
    @RunAfter(InitialLifecycleState.ON_RESUME)
    fun `load more action when loading more is disabled should send call load more interactor`() {
        reactorTest.runTest {
            trigger {
                getCurrentDataSourceStateSubject.onNext(
                        createPictureLoadingState(LoadingState.LOADING, createPicture(id = "1"), createPicture(id = "2"))
                )
            }

            sendAction { OnListEndReached }

            verify(mockLoadMoreFromDataSourceCompletabler, never()).execute()
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

    private fun createPictureLoadingState(loadingState: LoadingState, vararg pictures: Picture): PicturesWithLoadingState {
        return PicturesWithLoadingState(
                pictures = pictures.asList(),
                loadingState = loadingState
        )
    }

    private fun createState(
            isLoading: Boolean = false,
            isError: Boolean = false,
            isLoadingMoreEnabled: Boolean = false,
            pictures: List<Picture> = listOf(),
            isScrollToGloballySelectedPictureEnabled: Boolean = true,
            dataSourceType: DataSourceType = DataSourceType.ALL,
            locallySelectedPicture: Picture? = null,
            globallySelectedPicture: Picture? = null): PictureListState {
        return PictureListState(
                isLoading = isLoading,
                isError = isError,
                isLoadingMoreEnabled = isLoadingMoreEnabled,
                pictures = pictures,
                isScrollToGloballySelectedPictureEnabled = isScrollToGloballySelectedPictureEnabled,
                dataSourceType = dataSourceType,
                locallySelectedPicture = locallySelectedPicture,
                globallySelectedPicture = globallySelectedPicture
        )
    }
}