package com.sumera.argallery.ui.feature.picturedetails

import com.sumera.argallery.data.store.ui.model.Picture
import com.sumera.argallery.domain.datasource.GetCurrentDataSourceData
import com.sumera.argallery.domain.focusedpicture.SetFocusedPictureCompletabler
import com.sumera.argallery.tools.koreactor.ExecuteBehaviour
import com.sumera.argallery.tools.koreactor.ObserveBehaviour
import com.sumera.argallery.ui.feature.picturedetails.contract.OnPictureChanged
import com.sumera.argallery.ui.feature.picturedetails.contract.PictureDetailsState
import com.sumera.argallery.ui.feature.picturedetails.contract.ScrollToIndexEvent
import com.sumera.argallery.ui.feature.picturedetails.contract.SetPicturesDataReducer
import com.sumera.koreactor.behaviour.completable
import com.sumera.koreactor.behaviour.messages
import com.sumera.koreactor.behaviour.observable
import com.sumera.koreactor.behaviour.triggers
import com.sumera.koreactor.reactor.MviReactor
import com.sumera.koreactor.reactor.data.MviAction
import io.reactivex.Observable
import javax.inject.Inject

class PictureDetailsReactor @Inject constructor(
        private val getCurrentDataSourceData: GetCurrentDataSourceData,
        private val setFocusedPictureCompletabler: SetFocusedPictureCompletabler,
        private val initialPicture: Picture
) : MviReactor<PictureDetailsState>() {

    override fun createInitialState(): PictureDetailsState {
        return PictureDetailsState(pictures = listOf(), initialPicture = initialPicture)
    }

    override fun bind(actions: Observable<MviAction<PictureDetailsState>>) {
        val onNewFocusedPicture = actions.ofActionType<OnPictureChanged>()
                .skip(1)
                .flatMapSingle { action ->
                    stateSingle.map { it.pictures[action.newPictureIndex] }
                }

        ObserveBehaviour<Any, List<Picture>, PictureDetailsState>(
                triggers = triggers(attachLifecycleObservable),
                worker = observable { getCurrentDataSourceData.execute() },
                message = messages { SetPicturesDataReducer(newPictures = it) }
        ).bindToView()

        ExecuteBehaviour<Picture, PictureDetailsState>(
                triggers = triggers(onNewFocusedPicture),
                worker = completable { setFocusedPictureCompletabler.init(it).execute() }
        ).bindToView()

        // Scroll to initial picture position
        stateObservable
                .filter { it.pictures.isNotEmpty() }
                .take(1)
                .map { ScrollToIndexEvent(it.pictures.indexOf(it.initialPicture)) }
                .bindToView()
    }
}
