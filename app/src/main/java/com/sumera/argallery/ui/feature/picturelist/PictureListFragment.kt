package com.sumera.argallery.ui.feature.picturelist

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView.HORIZONTAL
import android.view.View
import com.sumera.argallery.R
import com.sumera.argallery.tools.centerItemPositions
import com.sumera.argallery.tools.extensions.Rx
import com.sumera.argallery.tools.extensions.calculateDiffUtilResult
import com.sumera.argallery.ui.base.BaseFragment
import com.sumera.argallery.ui.feature.main.contract.PictureListState
import com.sumera.argallery.ui.feature.picturedetails.PictureDetailsActivity
import com.sumera.argallery.ui.feature.picturelist.adapter.info.PictureInfoAdapter
import com.sumera.argallery.ui.feature.picturelist.adapter.picture.PictureListAdapter
import com.sumera.argallery.ui.feature.picturelist.contract.NavigateToPictureDetails
import com.sumera.argallery.ui.feature.picturelist.contract.OnFocusedItemChanged
import com.sumera.argallery.ui.feature.picturelist.contract.OnPictureClicked
import com.sumera.koreactor.reactor.MviReactor
import com.sumera.koreactor.reactor.data.MviEvent
import com.sumera.koreactor.util.data.asOptional
import com.sumera.koreactor.util.extension.getChange
import com.sumera.koreactor.util.extension.getNotNull
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

    override val layoutRes = R.layout.fragment_pictures_with_info

    override fun createReactor(): MviReactor<PictureListState> {
       return getReactor(reactorFactory, PictureListReactor::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pictureLayoutManager = LinearLayoutManager(context, HORIZONTAL, false)
        pictureList_infoRecycler.layoutManager = pictureLayoutManager
        pictureList_infoRecycler.adapter = pictureInfoAdapter

        pictureList_imageRecycler.setOrientation(DSVOrientation.HORIZONTAL)
        pictureList_imageRecycler.adapter = pictureImageAdapter
        pictureList_imageRecycler.setSlideOnFling(true)
        pictureList_imageRecycler.setItemTransformer(ScaleTransformer.Builder().setMinScale(0.8f).build())

        pictureList_imageRecycler.centerItemPositions()
                .throttleFirst(50, TimeUnit.MILLISECONDS)
                .map { OnFocusedItemChanged(it) }
                .bindToReactor()

        pictureImageAdapter.clicks()
                .map { OnPictureClicked(it) }
                .bindToReactor()
    }

    override fun bindToState(stateObservable: Observable<PictureListState>) {
        val picturesDataChange = stateObservable.getChange { it.pictures }
        val isLoadingChange = stateObservable.getChange { it.isLoading }
        val isErrorChange = stateObservable.getChange { it.isError }

        // Set picture recycler data
        Rx.combineLatestTriple(picturesDataChange, isLoadingChange, isErrorChange)
                .map { (pictures, isLoading, isError) ->
                    val data = pictures.map { PictureListAdapter.DataWrapper(it, PictureListAdapter.ViewHolderType.PICTURE) }.toMutableList()
                    if (isLoading) {
                        data.add(PictureListAdapter.DataWrapper(null, PictureListAdapter.ViewHolderType.LOADING))
                    }
                    if (isLoading) {
                        data.add(PictureListAdapter.DataWrapper(null, PictureListAdapter.ViewHolderType.ERROR))
                    }
                    data.toList()
                }
                .calculateDiffUtilResult()
                .observeState { (newData, diffResult) ->
                    pictureImageAdapter.setNewDataWithDiffUtil(newData, diffResult)
                }

        // Set picture info data
        stateObservable
                .getChange { it.pictures }
                .observeState {
                    pictureInfoAdapter.pictures = it
                }

        // Scroll info recycler to position of focused item
        stateObservable
                .getNotNull { it.focusedPicture.asOptional() }
                .flatMapSingle { stateObservable.firstOrError() }
                .observeState {
                    val position = it.pictures.indexOf(it.focusedPicture)
                    pictureList_infoRecycler.smoothScrollToPosition(position)
                }

        // Scroll picture recycler to position of focused item
        stateObservable
                .getChange { it.isScrollToFocusedItemEnabled }
                .flatMapSingle { stateObservable.firstOrError() }
                .observeState {
                    val position = it.pictures.indexOf(it.focusedPicture)
                    pictureList_imageRecycler.post {
                        pictureList_imageRecycler.scrollToPosition(position)
                    }
                }
    }

    override fun bindToEvent(eventsObservable: Observable<MviEvent<PictureListState>>) {
        eventsObservable.observeEvent { event ->
            when(event) {
                is NavigateToPictureDetails -> {
                    context?.let { startActivity(PictureDetailsActivity.getStartIntent(it)) }
                }
            }
        }
    }
}
