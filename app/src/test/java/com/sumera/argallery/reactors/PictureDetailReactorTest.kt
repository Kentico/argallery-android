package com.sumera.argallery.reactors

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import com.sumera.argallery.data.store.ui.model.Picture
import com.sumera.argallery.domain.UnityImagePrepareCompletabler
import com.sumera.argallery.domain.favourites.AddFavouritePictureCompletabler
import com.sumera.argallery.domain.favourites.IsFavouriteObserver
import com.sumera.argallery.domain.favourites.RemoveFavouritePictureCompletabler
import com.sumera.argallery.ui.feature.picturedetail.PictureDetailReactor
import com.sumera.argallery.ui.feature.picturedetail.contract.AugmentedRealityClicked
import com.sumera.argallery.ui.feature.picturedetail.contract.NavigateToAugmentedReality
import com.sumera.argallery.ui.feature.picturedetail.contract.PictureDetailState
import com.sumera.argallery.ui.feature.picturedetail.contract.ToggleFavouriteAction
import com.sumera.koreactor.reactor.MviReactor
import com.sumera.koreactorexampleapp.lib.ReactorTestRule
import com.sumera.koreactorexampleapp.lib.annotation.InitialLifecycleState
import com.sumera.koreactorexampleapp.lib.annotation.RunAfter
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString

class PictureDetailReactorTest {

    val mockIsFavouriteObserver = mock<IsFavouriteObserver>()
    val mockAddFavouritePictureCompletabler = mock<AddFavouritePictureCompletabler>()
    val mockRemoveFavouritePictureCompletabler = mock<RemoveFavouritePictureCompletabler>()
    val mockUnityImagePrepareCompletabler = mock<UnityImagePrepareCompletabler>()
    val mockPicture = createPicture(id = "100")

    lateinit var isFavouriteSubject: PublishSubject<Boolean>
    lateinit var addFavouritePictureSubject: PublishSubject<Unit>
    lateinit var removeFavouritePictureSubject: PublishSubject<Unit>
    lateinit var unityImagePrepareSubject: PublishSubject<Unit>

    @Rule
    @JvmField
    val reactorTest = object : ReactorTestRule<PictureDetailState>() {
        override fun createNewReactorInstance(): MviReactor<PictureDetailState> {
            whenever(mockIsFavouriteObserver.execute()).thenAnswer {
                isFavouriteSubject = PublishSubject.create<Boolean>()
                return@thenAnswer isFavouriteSubject
            }

            whenever(mockAddFavouritePictureCompletabler.execute()).thenAnswer {
                addFavouritePictureSubject = PublishSubject.create<Unit>()
                return@thenAnswer  Single.fromObservable(addFavouritePictureSubject.take(1)).toCompletable()
            }

            whenever(mockRemoveFavouritePictureCompletabler.execute()).thenAnswer {
                removeFavouritePictureSubject = PublishSubject.create<Unit>()
                return@thenAnswer Single.fromObservable(removeFavouritePictureSubject.take(1)).toCompletable()
            }

            whenever(mockUnityImagePrepareCompletabler.execute()).thenAnswer {
                unityImagePrepareSubject = PublishSubject.create<Unit>()
                return@thenAnswer Single.fromObservable(unityImagePrepareSubject.take(1)).toCompletable()
            }

            whenever(mockIsFavouriteObserver.init(anyString())).thenReturn(mockIsFavouriteObserver)
            whenever(mockAddFavouritePictureCompletabler.init(mockPicture)).thenReturn(mockAddFavouritePictureCompletabler)
            whenever(mockRemoveFavouritePictureCompletabler.init(mockPicture)).thenReturn(mockRemoveFavouritePictureCompletabler)
            whenever(mockUnityImagePrepareCompletabler.init(any())).thenReturn(mockUnityImagePrepareCompletabler)

            return PictureDetailReactor(
                    mockIsFavouriteObserver,
                    mockAddFavouritePictureCompletabler,
                    mockRemoveFavouritePictureCompletabler,
                    mockUnityImagePrepareCompletabler,
                    mockPicture
            )
        }
    }

    @Test
    @RunAfter(InitialLifecycleState.ON_RESUME)
    fun `check default state`() {
        reactorTest.runTest {
            assertNextState { PictureDetailState(mockPicture, false) }
        }
    }

    @Test
    @RunAfter(InitialLifecycleState.ON_RESUME)
    fun `when augmented reality button is clicked then image should be prepared and navigate to ar should be send`() {
        reactorTest.runTest {
            sendAction { AugmentedRealityClicked }

            trigger { unityImagePrepareSubject.onNext(Unit) }

            assertNextEvent { NavigateToAugmentedReality }

            verify(mockUnityImagePrepareCompletabler).init(mockPicture.imageUrl)
            verify(mockUnityImagePrepareCompletabler).execute()
        }
    }

    @Test
    @RunAfter(InitialLifecycleState.ON_RESUME)
    fun `when is favourite interactor emit data then it should be propagated to the state`() {
        reactorTest.runTest {
            trigger { isFavouriteSubject.onNext(true) }

            assertLastState { PictureDetailState(mockPicture, true) }
        }
    }

    @Test
    @RunAfter(InitialLifecycleState.ON_RESUME)
    fun `when toggle favourite action is send with favorited picture then add favorite picture should be called`() {
        reactorTest.runTest {
            trigger { isFavouriteSubject.onNext(true) }

            sendAction { ToggleFavouriteAction }

            trigger { removeFavouritePictureSubject.onNext(Unit) }

            verify(mockRemoveFavouritePictureCompletabler).init(mockPicture)
            verify(mockRemoveFavouritePictureCompletabler).execute()
        }
    }

    @Test
    @RunAfter(InitialLifecycleState.ON_RESUME)
    fun `when toggle favourite action is send with unfavorited picture then remove favorite picture should be called`() {
        reactorTest.runTest {
            trigger { isFavouriteSubject.onNext(false) }

            sendAction { ToggleFavouriteAction }

            trigger { addFavouritePictureSubject.onNext(Unit) }

            verify(mockAddFavouritePictureCompletabler).init(mockPicture)
            verify(mockAddFavouritePictureCompletabler).execute()
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