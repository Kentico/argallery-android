package com.sumera.argallery.ui.feature.picturedetail

import android.os.Bundle
import com.bumptech.glide.Glide
import com.sumera.argallery.R
import com.sumera.argallery.data.store.ui.model.Picture
import com.sumera.argallery.ui.base.BaseFragment
import com.sumera.argallery.ui.feature.picturedetail.contract.PictureDetailState
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

    override fun bindToState(stateObservable: Observable<PictureDetailState>) {
        stateObservable.getChange { it.picture }
                .observeState { picture ->
                    context?.let {
                        Glide.with(it)
                                .load(picture.imageUrl)
                                .into(pictureDetail_image)
                    }
                }
    }
}