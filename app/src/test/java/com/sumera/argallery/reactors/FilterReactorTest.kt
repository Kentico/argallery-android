package com.sumera.argallery.reactors

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import com.sumera.argallery.data.store.ui.model.Filter
import com.sumera.argallery.domain.filter.GetCurrentFilterSingler
import com.sumera.argallery.domain.filter.SetCurrentFilterCompletabler
import com.sumera.argallery.ui.feature.filter.FilterReactor
import com.sumera.argallery.ui.feature.filter.contract.CloseEvent
import com.sumera.argallery.ui.feature.filter.contract.FilterState
import com.sumera.argallery.ui.feature.filter.contract.OnCloseButtonClicked
import com.sumera.argallery.ui.feature.filter.contract.OnFirstCategoryStateChanged
import com.sumera.argallery.ui.feature.filter.contract.OnPriceRangeChanged
import com.sumera.argallery.ui.feature.filter.contract.OnResetButtonClicked
import com.sumera.argallery.ui.feature.filter.contract.OnSecondCategoryStateChanged
import com.sumera.argallery.ui.feature.filter.contract.OnYearRangeChanged
import com.sumera.koreactor.reactor.MviReactor
import com.sumera.koreactorexampleapp.lib.ReactorTestRule
import com.sumera.koreactorexampleapp.lib.annotation.InitialLifecycleState
import com.sumera.koreactorexampleapp.lib.annotation.RunAfter
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class FilterReactorTest {

    var mockSetCurrentFilterCompletabler = mock<SetCurrentFilterCompletabler>()
    var mockGetGetCurrentFilterSingler = mock<GetCurrentFilterSingler>()

    lateinit var setCurrentFilterSubject: PublishSubject<Unit>
    lateinit var getCurrentFilterSubject: PublishSubject<Filter>

    @Rule
    @JvmField
    val reactorTest = object : ReactorTestRule<FilterState>() {
        override fun createNewReactorInstance(): MviReactor<FilterState> {
            whenever(mockSetCurrentFilterCompletabler.execute()).thenAnswer {
                setCurrentFilterSubject = PublishSubject.create<Unit>()
                return@thenAnswer Single.fromObservable(setCurrentFilterSubject.take(1)).toCompletable()
            }

            whenever(mockGetGetCurrentFilterSingler.execute()).thenAnswer {
                getCurrentFilterSubject = PublishSubject.create<Filter>()
                return@thenAnswer getCurrentFilterSubject.take(1).firstOrError()
            }

            return FilterReactor(
                    mockGetGetCurrentFilterSingler,
                    mockSetCurrentFilterCompletabler
            )
        }
    }

    @Before
    fun setUp() {
        whenever(mockSetCurrentFilterCompletabler.init(any())).thenReturn(mockSetCurrentFilterCompletabler)
    }

    @Test
    @RunAfter(InitialLifecycleState.ON_RESUME)
    fun `filter should have default value from beginning`() {
        reactorTest.runTest {
            assertNextState { FilterState(currentFilter = Filter.createDefault()) }
        }
    }

    @Test
    @RunAfter(InitialLifecycleState.ON_RESUME)
    fun `first category change action should change first category in the state`() {
        reactorTest.runTest {
            sendAction { OnFirstCategoryStateChanged(true) }

            assertLastState { FilterState(currentFilter = Filter.createDefault().copy(firstCategoryEnabled = true)) }

            sendAction { OnFirstCategoryStateChanged(false) }

            assertLastState { FilterState(currentFilter = Filter.createDefault().copy(firstCategoryEnabled = false)) }
        }
    }

    @Test
    @RunAfter(InitialLifecycleState.ON_RESUME)
    fun `second category change action should change second category in the state`() {
        reactorTest.runTest {
            sendAction { OnSecondCategoryStateChanged(true) }

            assertLastState { FilterState(currentFilter = Filter.createDefault().copy(secondCategoryEnabled = true)) }

            sendAction { OnSecondCategoryStateChanged(false) }

            assertLastState { FilterState(currentFilter = Filter.createDefault().copy(secondCategoryEnabled = false)) }
        }
    }

    @Test
    @RunAfter(InitialLifecycleState.ON_RESUME)
    fun `price range change action should change min and max price in the state`() {
        reactorTest.runTest {
            sendAction { OnPriceRangeChanged(100, 500) }

            assertLastState { FilterState(currentFilter = Filter.createDefault().copy(minPrice = 100, maxPrice = 500)) }

            sendAction { OnPriceRangeChanged(200, 450) }

            assertLastState { FilterState(currentFilter = Filter.createDefault().copy(minPrice = 200, maxPrice = 450)) }
        }
    }

    @Test
    @RunAfter(InitialLifecycleState.ON_RESUME)
    fun `year range change action should change min and max year in the state`() {
        reactorTest.runTest {
            sendAction { OnYearRangeChanged(1900, 1920) }

            assertLastState { FilterState(currentFilter = Filter.createDefault().copy(minYear = 1900, maxYear = 1920)) }

            sendAction { OnYearRangeChanged(1905, 1906) }

            assertLastState { FilterState(currentFilter = Filter.createDefault().copy(minYear = 1905, maxYear = 1906)) }
        }
    }

    @Test
    @RunAfter(InitialLifecycleState.ON_RESUME)
    fun `reset action should reset filter to default`() {
        reactorTest.runTest {
            sendAction { OnFirstCategoryStateChanged(false) }
            sendAction { OnSecondCategoryStateChanged(false) }
            sendAction { OnYearRangeChanged(1900, 1920) }
            sendAction { OnPriceRangeChanged(100, 500) }

            assertLastState { FilterState(currentFilter = Filter.createDefault().copy(
                    minYear = 1900, maxYear = 1920, minPrice = 100, maxPrice = 500, firstCategoryEnabled = false, secondCategoryEnabled = false
            )) }

            sendAction { OnResetButtonClicked }

            assertLastState { FilterState(currentFilter = Filter.createDefault()) }
        }
    }

    @Test
    @RunAfter(InitialLifecycleState.ON_RESUME)
    fun `close button should save button and send close event`() {
        reactorTest.runTest {
            sendAction { OnCloseButtonClicked }

            trigger { setCurrentFilterSubject.onNext(Unit) }

            assertNextEvent { CloseEvent }

            verify(mockSetCurrentFilterCompletabler).init(Filter.createDefault())
            verify(mockSetCurrentFilterCompletabler).execute()
        }
    }

    @Test
    @RunAfter(InitialLifecycleState.ON_RESUME)
    fun `new filter from get current filter singler should set filter in the state`() {
        reactorTest.runTest {
            sendAction { OnCloseButtonClicked }

            trigger { getCurrentFilterSubject.onNext(Filter.createDefault().copy(minPrice = 1, maxPrice = 2)) }

            assertLastState { FilterState(Filter.createDefault().copy(minPrice = 1, maxPrice = 2)) }
        }
    }


}