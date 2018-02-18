package com.sumera.argallery.ui.feature.picturedetail

import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.jakewharton.rxbinding2.view.clicks
import com.sumera.argallery.R
import com.sumera.argallery.data.store.ui.model.Picture
import com.sumera.argallery.ui.base.BaseFragment
import com.sumera.argallery.ui.feature.picturedetail.contract.PictureDetailState
import com.sumera.argallery.ui.feature.picturedetail.contract.TogglFavouriteAction
import com.sumera.koreactor.reactor.MviReactor
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


        pictureDetail_favourite.clicks()
                .map { TogglFavouriteAction() }
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