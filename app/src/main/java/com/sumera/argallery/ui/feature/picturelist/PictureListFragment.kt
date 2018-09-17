package com.sumera.argallery.ui.feature.picturelist

import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.constraint.ConstraintSet
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView.HORIZONTAL
import android.transition.ChangeBounds
import android.transition.TransitionManager
import android.view.View
import com.jakewharton.rxbinding2.support.v7.widget.dataChanges
import com.jakewharton.rxbinding2.view.clicks
import com.sumera.argallery.R
import com.sumera.argallery.data.store.ui.datasource.model.DataSourceType
import com.sumera.argallery.data.store.ui.model.Picture
import com.sumera.argallery.tools.extensions.calculateDiffUtilResult
import com.sumera.argallery.tools.extensions.setVisibile
import com.sumera.argallery.tools.observables.centerItemPositions
import com.sumera.argallery.ui.Navigator
import com.sumera.argallery.ui.base.BaseFragment
import com.sumera.argallery.ui.feature.picturedetails.PictureDetailsActivity
import com.sumera.argallery.ui.feature.picturelist.adapter.info.PictureInfoAdapter
import com.sumera.argallery.ui.feature.picturelist.adapter.picture.PictureListAdapter
import com.sumera.argallery.ui.feature.picturelist.contract.NavigateToFilter
import com.sumera.argallery.ui.feature.picturelist.contract.NavigateToPictureDetails
import com.sumera.argallery.ui.feature.picturelist.contract.OnChangeFilterClicked
import com.sumera.argallery.ui.feature.picturelist.contract.OnFocusedIndexChanged
import com.sumera.argallery.ui.feature.picturelist.contract.OnListEndReached
import com.sumera.argallery.ui.feature.picturelist.contract.OnPictureClicked
import com.sumera.argallery.ui.feature.picturelist.contract.OnShowAllClicked
import com.sumera.argallery.ui.feature.picturelist.contract.OnTryAgainClicked
import com.sumera.argallery.ui.feature.picturelist.contract.PictureListState
import com.sumera.argallery.ui.feature.picturelist.contract.hasEmptyState
import com.sumera.koreactor.reactor.MviReactor
import com.sumera.koreactor.reactor.data.MviEvent
import com.sumera.koreactor.util.data.asOptional
import com.sumera.koreactor.util.extension.getChange
import com.sumera.koreactor.util.extension.getNotNull
import com.sumera.koreactor.util.extension.getTrue
import com.yarolegovich.discretescrollview.DSVOrientation
import com.yarolegovich.discretescrollview.transform.ScaleTransformer
import io.reactivex.Observable
import kotlinx.android.synthetic.main.fragment_pictures_with_info.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class PictureListFragment : BaseFragment<PictureListState>() {

    @Inject lateinit var reactorFactory: PictureListReactorFactory
    @Inject lateinit var pictureImageAdapter: PictureListAdapter
    @Inject lateinit var pictureInfoAdapter: PictureInfoAdapter
    @Inject lateinit var navigator: Navigator

    override val layoutRes = R.layout.fragment_pictures_with_info

    override fun createReactor(): MviReactor<PictureListState> {
       return getReactor(reactorFactory, PictureListReactor::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pictureLayoutManager = LinearLayoutManager(context, HORIZONTAL, false)
        pictureList_infoRecycler.layoutManager = pictureLayoutManager
        pictureList_infoRecycler.adapter = pictureInfoAdapter
        pictureList_infoRecycler.setOnTouchListener { _, _ -> true }

        pictureList_imageRecycler.setOrientation(DSVOrientation.HORIZONTAL)
        pictureList_imageRecycler.adapter = pictureImageAdapter
        pictureList_imageRecycler.setSlideOnFling(true)
        pictureList_imageRecycler.setItemTransformer(ScaleTransformer.Builder().setMinScale(0.8f).build())
        pictureList_imageRecycler.setOverScrollEnabled(true)

        pictureList_imageRecycler.centerItemPositions()
                .throttleFirst(50, TimeUnit.MILLISECONDS)
                .map { OnFocusedIndexChanged(it) }
                .bindToReactor()

        pictureList_changeFilter.clicks()
                .map { OnChangeFilterClicked }
                .bindToReactor()

        pictureList_moveToAll.clicks()
                .map { OnShowAllClicked }
                .bindToReactor()

        pictureImageAdapter.dataChanges()
                .map { pictureList_imageRecycler.currentItem }
                .map { OnFocusedIndexChanged(it) }
                .bindToReactor()

        pictureImageAdapter.onListEndReached()
                .map { OnListEndReached }
                .bindToReactor()

        pictureImageAdapter.clicks()
                .map { OnPictureClicked(it) }
                .bindToReactor()

        pictureImageAdapter.errorClicks()
                .map { OnTryAgainClicked }
                .bindToReactor()
    }

    override fun bindToState(stateObservable: Observable<PictureListState>) {
        val loadingErrorOrDataStateChanged = stateObservable
                .distinctUntilChanged { previous, new ->
                    previous.pictures == new.pictures &&
                            previous.isLoading == new.isLoading &&
                            previous.isError == new.isError
                }.share()

        val loadingErrorDataOrSourceTypeStateChanged = stateObservable
                .distinctUntilChanged { previous, new ->
                            previous.pictures == new.pictures &&
                            previous.isLoading == new.isLoading &&
                            previous.isError == new.isError &&
                            previous.dataSourceType == new.dataSourceType
                }.share()

        // Scroll to start when data source changes
        stateObservable.getChange { it.dataSourceType }
                .observeState {
                    pictureImageAdapter.notifyDataSetChanged()
                    pictureList_imageRecycler.post {
                        pictureList_imageRecycler.scrollToPosition(0)
                    }
                }

        // Set picture recycler data
        loadingErrorOrDataStateChanged
                .map { state-> createAdapterData(state.pictures, state.isLoading, state.isError)}
                .calculateDiffUtilResult()
                .observeState { (newData) ->
                    pictureImageAdapter.setNewData(newData)
                }

        // Show favourites empty state
        loadingErrorDataOrSourceTypeStateChanged
                .getTrue { it.hasEmptyState() && it.dataSourceType == DataSourceType.FAVOURITES }
                .observeState {
                    showEmptyFavourites()
                }

        // Hide favourites empty state
        loadingErrorDataOrSourceTypeStateChanged
                .getTrue { it.hasEmptyState().not() || it.dataSourceType != DataSourceType.FAVOURITES }
                .observeState {
                        hideEmptyFavourites()
                }

        // Show filtered empty state
        loadingErrorDataOrSourceTypeStateChanged
                .getTrue { it.hasEmptyState() && it.dataSourceType == DataSourceType.FILTERED }
                .observeState {
                    showEmptyFiltered()
                }

        // Hide filtered empty state
        loadingErrorDataOrSourceTypeStateChanged
                .getTrue { it.hasEmptyState().not() || it.dataSourceType != DataSourceType.FILTERED }
                .observeState {
                    hideEmptyFiltered()
                }

        // Set picture info data
        stateObservable
                .getChange { it.pictures }
                .observeState {
                    pictureInfoAdapter.pictures = it
                }

        // Scroll info recycler to position of locally selected picture
        // (picture which was selected on current screen)
        stateObservable
                .getNotNull { it.locallySelectedPicture.asOptional() }
                .flatMapSingle { stateObservable.firstOrError() }
                .observeState {
                    val position = it.pictures.indexOf(it.locallySelectedPicture)
                    if (position != -1) {
                        pictureList_infoRecycler.smoothScrollToPosition(position)
                    }
                }

        // Scroll picture recycler to position of globally selected picture
        // (picture which was selected on different screen)
        stateObservable
                .getChange { it.isScrollToGloballySelectedPictureEnabled }
                .flatMapSingle { stateObservable.firstOrError() }
                .observeState {
                    val position = it.pictures.indexOf(it.globallySelectedPicture)
                    pictureList_imageRecycler.post {
                        pictureList_imageRecycler.scrollToPosition(position)
                    }
                }

        // Hide info content when pictures are empty
        stateObservable
                .getTrue { it.pictures.isEmpty() }
                .observeState {
                    hideInfoRecycler()
                }

        // Show info content when pictures are present
        stateObservable
                .getTrue { it.pictures.isNotEmpty() }
                .observeState {
                    showInfoRecycler()
                }
    }

    override fun bindToEvent(eventsObservable: Observable<MviEvent<PictureListState>>) {
        eventsObservable.observeEvent { event ->
            when(event) {
                is NavigateToPictureDetails -> {
                    context?.let { startActivity(PictureDetailsActivity.getStartIntent(it, event.picture)) }
                }
                NavigateToFilter -> {
                    navigator.navigateToFilter()
                }
            }
        }
    }

    private fun createAdapterData(pictures: List<Picture>, isLoading: Boolean, isError: Boolean): List<PictureListAdapter.DataWrapper> {
        val data = pictures.map { PictureListAdapter.DataWrapper(it, PictureListAdapter.ViewHolderType.PICTURE) }.toMutableList()
        if (isLoading) {
            data.add(PictureListAdapter.DataWrapper(null, PictureListAdapter.ViewHolderType.LOADING))
        }
        if (isError) {
            data.add(PictureListAdapter.DataWrapper(null, PictureListAdapter.ViewHolderType.ERROR))
        }
        return data.toList()
    }

    private fun showEmptyFavourites() {
        TransitionManager.beginDelayedTransition(pictureList_emptyViewFavourites, ChangeBounds())
        pictureList_emptyViewFavourites.setVisibile(true)
    }

    private fun hideEmptyFavourites() {
        TransitionManager.beginDelayedTransition(pictureList_emptyViewFavourites, ChangeBounds())
        pictureList_emptyViewFavourites.setVisibile(false)
    }

    private fun showEmptyFiltered() {
        TransitionManager.beginDelayedTransition(pictureList_emptyViewFiltered, ChangeBounds())
        pictureList_emptyViewFiltered.setVisibile(true)
    }

    private fun hideEmptyFiltered() {
        TransitionManager.beginDelayedTransition(pictureList_emptyViewFiltered, ChangeBounds())
        pictureList_emptyViewFiltered.setVisibile(false)
    }

    private fun showInfoRecycler() {
        val constraintLayout = view as? ConstraintLayout ?: throw IllegalStateException("Constraint layout should be set as root")
        val constraintSet = ConstraintSet()
        constraintSet.clone(constraintLayout)
        constraintSet.setGuidelinePercent(R.id.pictureList_recyclerGuideline, 0.3f)
        TransitionManager.beginDelayedTransition(constraintLayout, ChangeBounds())
        constraintSet.applyTo(constraintLayout)
    }

    private fun hideInfoRecycler() {
        val constraintLayout = view as? ConstraintLayout ?: throw IllegalStateException("Constraint layout should be set as root")
        val constraintSet = ConstraintSet()
        constraintSet.clone(constraintLayout)
        constraintSet.setGuidelinePercent(R.id.pictureList_recyclerGuideline, 0.3f)
        TransitionManager.beginDelayedTransition(constraintLayout, ChangeBounds())
        constraintSet.applyTo(constraintLayout)
    }
}
