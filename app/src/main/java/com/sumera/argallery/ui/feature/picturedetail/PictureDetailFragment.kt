package com.sumera.argallery.ui.feature.picturedetail

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.jakewharton.rxbinding2.view.clicks
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import com.sumera.argallery.R
import com.sumera.argallery.data.store.ui.model.Picture
import com.sumera.argallery.ui.base.BaseFragment
import com.sumera.argallery.ui.feature.picturedetail.contract.AugmentedRealityClicked
import com.sumera.argallery.ui.feature.picturedetail.contract.NavigateToAugmentedReality
import com.sumera.argallery.ui.feature.picturedetail.contract.PictureDetailState
import com.sumera.argallery.ui.feature.picturedetail.contract.ToggleFavouriteAction
import com.sumera.argallery.ui.feature.unityactivity.UnityStarterActivity
import com.sumera.koreactor.reactor.MviReactor
import com.sumera.koreactor.reactor.data.MviEvent
import com.sumera.koreactor.util.extension.getChange
import io.reactivex.Observable
import kotlinx.android.synthetic.main.fragment_picture_detail.*
import javax.inject.Inject

class PictureDetailFragment : BaseFragment<PictureDetailState>() {

    @Inject lateinit var reactorFactory: PictureDetailReactorFactory

    companion object {
        val pictureKey = "picture_key"

        fun newInstance(picture: Picture): PictureDetailFragment {
            return PictureDetailFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(pictureKey, picture)
                }
            }
        }
    }

    override val layoutRes = R.layout.fragment_picture_detail

    override fun createReactor(): MviReactor<PictureDetailState> {
        return getReactor(reactorFactory, PictureDetailReactor::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pictureDetail_slidingLayout.addPanelSlideListener(object : SlidingUpPanelLayout.SimplePanelSlideListener() {
            override fun onPanelSlide(panel: View, slideOffset: Float) {
                pictureDetail_swipeIndicatorArrow.rotationX = 180 * slideOffset
            }

            override fun onPanelStateChanged(panel: View, previousState: SlidingUpPanelLayout.PanelState, newState: SlidingUpPanelLayout.PanelState) {}
        })

        pictureDetail_favourite.clicks()
                .map { ToggleFavouriteAction }
                .bindToReactor()

        pictureDetail_augmentedReality.clicks()
                .map { AugmentedRealityClicked }
                .bindToReactor()
    }

    override fun bindToState(stateObservable: Observable<PictureDetailState>) {
        stateObservable.getChange { it.picture }
                .observeState { picture ->
                    context?.let {
                        Glide.with(it)
                                .load(picture.imageUrl)
                                .into(pictureDetail_image)
                    }

                    pictureDetail_title.text = picture.title
                    pictureDetail_description.text = picture.description
                }

        // Set favourite state without animation
        stateObservable.getChange { it.isFavourite }
                .take(1)
                .observeState { isFavourite ->
                    setFavouriteIcon(isFavourite, withAnimation = false)
                }

        // Set favourite state with animation
        stateObservable.getChange { it.isFavourite }
                .skip(1)
                .observeState { isFavourite ->
                    setFavouriteIcon(isFavourite, withAnimation = false)
                }
    }

    override fun bindToEvent(eventsObservable: Observable<MviEvent<PictureDetailState>>) {
        eventsObservable.observeEvent { event ->
            when (event) {
                NavigateToAugmentedReality -> {
                    val intent = Intent(activity, UnityStarterActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }

    private fun setFavouriteIcon(isFavourite: Boolean, withAnimation: Boolean) {
        if (withAnimation) {
            // TODO
        } else {
            if (isFavourite) {
                pictureDetail_favourite.setImageResource(R.drawable.ic_favourite_full)
            } else {
                pictureDetail_favourite.setImageResource(R.drawable.ic_favourite_empty)
            }
        }
    }
}