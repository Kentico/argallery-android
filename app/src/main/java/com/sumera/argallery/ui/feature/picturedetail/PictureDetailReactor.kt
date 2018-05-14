package com.sumera.argallery.ui.feature.picturedetail

import com.sumera.argallery.data.store.ui.model.Picture
import com.sumera.argallery.domain.favourites.AddFavouritePictureCompletabler
import com.sumera.argallery.domain.favourites.IsFavouriteObserver
import com.sumera.argallery.domain.favourites.RemoveFavouritePictureCompletabler
import com.sumera.argallery.tools.koreactor.ExecuteBehaviour
import com.sumera.argallery.tools.koreactor.ObserveBehaviour
import com.sumera.argallery.ui.feature.picturedetail.contract.AugmentedRealityClicked
import com.sumera.argallery.ui.feature.picturedetail.contract.NavigateToAugmentedReality
import com.sumera.argallery.ui.feature.picturedetail.contract.PictureDetailState
import com.sumera.argallery.ui.feature.picturedetail.contract.SetFavourite
import com.sumera.argallery.ui.feature.picturedetail.contract.ToggleFavouriteAction
import com.sumera.koreactor.behaviour.completable
import com.sumera.koreactor.behaviour.messages
import com.sumera.koreactor.behaviour.observable
import com.sumera.koreactor.behaviour.triggers
import com.sumera.koreactor.reactor.MviReactor
import com.sumera.koreactor.reactor.data.MviAction
import io.reactivex.Observable
import javax.inject.Inject

class PictureDetailReactor @Inject constructor(
        private val isFavouriteObserver: IsFavouriteObserver,
        private val addFavouritePictureCompletabler: AddFavouritePictureCompletabler,
        private val removeFavouritePictureCompletabler: RemoveFavouritePictureCompletabler,
        private val picture: Picture
) : MviReactor<PictureDetailState>() {

    override fun createInitialState(): PictureDetailState {
       return PictureDetailState(picture = picture, isFavourite = false)
    }

    override fun bind(actions: Observable<MviAction<PictureDetailState>>) {
        val pictureFavouriteAction = actions.ofActionType<ToggleFavouriteAction>()
                .flatMapSingle { stateSingle.map { it.isFavourite } }
                .filter { !it }

        val pictureUnfavouriteAction = actions.ofActionType<ToggleFavouriteAction>()
                .flatMapSingle { stateSingle.map { it.isFavourite } }
                .filter { it }

        val augmentedRealityClicked = actions.ofActionType<AugmentedRealityClicked>()

        // Navigate to augmented reality screen
        augmentedRealityClicked
                .map { NavigateToAugmentedReality }
                .bindToView()

        // Add picture to favourites
        ExecuteBehaviour<Boolean, PictureDetailState>(
                triggers = triggers(pictureFavouriteAction),
                worker = completable { addFavouritePictureCompletabler.init(picture).execute() }
        ).bindToView()

        // Remove picture from favourites
        ExecuteBehaviour<Boolean, PictureDetailState>(
                triggers = triggers(pictureUnfavouriteAction),
                worker = completable { removeFavouritePictureCompletabler.init(picture).execute() }
        ).bindToView()

        // Observe favourite state
        ObserveBehaviour<Any, Boolean, PictureDetailState>(
                triggers = triggers(attachLifecycleObservable),
                worker = observable { isFavouriteObserver.init(picture.id).execute() },
                message = messages { SetFavourite(it) }
        ).bindToView()
    }
}