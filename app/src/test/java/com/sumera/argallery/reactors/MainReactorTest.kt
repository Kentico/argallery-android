package com.sumera.argallery.reactors

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import com.sumera.argallery.data.store.ui.datasource.model.DataSourceType
import com.sumera.argallery.domain.datasource.GetCurrentDataSourceTypeObserver
import com.sumera.argallery.domain.datasource.SetCurrentDataSourceCompletabler
import com.sumera.argallery.ui.feature.main.MainReactor
import com.sumera.argallery.ui.feature.main.contract.MainState
import com.sumera.argallery.ui.feature.main.contract.OnTabClickedAction
import com.sumera.koreactor.reactor.MviReactor
import com.sumera.koreactorexampleapp.lib.ReactorTestRule
import com.sumera.koreactorexampleapp.lib.annotation.InitialLifecycleState
import com.sumera.koreactorexampleapp.lib.annotation.RunAfter
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MainReactorTest {

    var mockSetCurrentDataSourceTypeCompletabler = mock<SetCurrentDataSourceCompletabler>()
    var mockGetCurrentDataSourceTypeObserver = mock<GetCurrentDataSourceTypeObserver>()

    lateinit var setDataSourceSubject: PublishSubject<Unit>
    lateinit var getDataSourceSubject: PublishSubject<DataSourceType>

    @Rule
    @JvmField
    val reactorTest = object : ReactorTestRule<MainState>() {
        override fun createNewReactorInstance(): MviReactor<MainState> {
            whenever(mockSetCurrentDataSourceTypeCompletabler.execute()).thenAnswer {
                setDataSourceSubject = PublishSubject.create<Unit>()
                return@thenAnswer Single.fromObservable(setDataSourceSubject.take(1)).toCompletable()
            }

            whenever(mockGetCurrentDataSourceTypeObserver.execute()).thenAnswer {
                getDataSourceSubject = PublishSubject.create<DataSourceType>()
                return@thenAnswer getDataSourceSubject
            }

            return MainReactor(
                    mockSetCurrentDataSourceTypeCompletabler,
                    mockGetCurrentDataSourceTypeObserver
            )
        }
    }

    @Before
    fun setUp() {
        whenever(mockSetCurrentDataSourceTypeCompletabler.init(any())).thenReturn(mockSetCurrentDataSourceTypeCompletabler)
    }

    @Test
    @RunAfter(InitialLifecycleState.ON_RESUME)
    fun `data source should be set to ALL by default`() {
        reactorTest.runTest {
            assertNextState { MainState(dataSourceType = DataSourceType.ALL) }
        }
    }

    @Test
    @RunAfter(InitialLifecycleState.ON_RESUME)
    fun `on tab clicked should set new filtered data source`() {
        reactorTest.runTest {
            sendAction { OnTabClickedAction(DataSourceType.FILTERED) }

            verify(mockSetCurrentDataSourceTypeCompletabler).init(DataSourceType.FILTERED)
            verify(mockSetCurrentDataSourceTypeCompletabler).execute()
        }
    }

    @Test
    @RunAfter(InitialLifecycleState.ON_RESUME)
    fun `on tab clicked should set new all data source`() {
        reactorTest.runTest {
            sendAction { OnTabClickedAction(DataSourceType.ALL) }

            verify(mockSetCurrentDataSourceTypeCompletabler).init(DataSourceType.ALL)
            verify(mockSetCurrentDataSourceTypeCompletabler).execute()
        }
    }

    @Test
    @RunAfter(InitialLifecycleState.ON_RESUME)
    fun `on tab clicked should set new favourites data source`() {
        reactorTest.runTest {
            sendAction { OnTabClickedAction(DataSourceType.FAVOURITES) }

            verify(mockSetCurrentDataSourceTypeCompletabler).init(DataSourceType.FAVOURITES)
            verify(mockSetCurrentDataSourceTypeCompletabler).execute()
        }
    }

    @Test
    @RunAfter(InitialLifecycleState.ON_RESUME)
    fun `get data source new item should set data source in state`() {
        reactorTest.runTest {
            assertNextState { MainState(dataSourceType = DataSourceType.ALL) }

            trigger { getDataSourceSubject.onNext(DataSourceType.FAVOURITES) }

            assertNextState { MainState(dataSourceType = DataSourceType.FAVOURITES) }

            trigger { getDataSourceSubject.onNext(DataSourceType.FILTERED) }

            assertNextState { MainState(dataSourceType = DataSourceType.FILTERED) }

            trigger { getDataSourceSubject.onNext(DataSourceType.ALL) }

            assertNextState { MainState(dataSourceType = DataSourceType.ALL) }
        }
    }
}