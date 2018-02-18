package com.sumera.argallery.ui.feature.picturedetails

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.WindowManager
import com.jakewharton.rxbinding2.support.v4.view.pageSelections
import com.sumera.argallery.R
import com.sumera.argallery.ui.base.BaseActivity
import com.sumera.argallery.ui.feature.picturedetails.adapter.PictureDetailsAdapter
import com.sumera.argallery.ui.feature.picturedetails.contract.OnPictureChanged
import com.sumera.argallery.ui.feature.picturedetails.contract.PictureDetailsState
import com.sumera.koreactor.reactor.MviReactor
import com.sumera.koreactor.util.extension.getChange
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_picture_details.*
import javax.inject.Inject



class PictureDetailsActivity : BaseActivity<PictureDetailsState>() {

    @Inject lateinit var reactorFactory: PictureDetailsReactorFactory
    @Inject lateinit var pictureDetailsAdapter: PictureDetailsAdapter

    companion object {
        fun getStartIntent(context: Context): Intent {
            return Intent(context, PictureDetailsActivity::class.java)
        }
    }

    override val layoutRes = R.layout.activity_picture_details

    override fun createReactor(): MviReactor<PictureDetailsState> {
        return getReactor(reactorFactory, PictureDetailsReactor::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.black)

        pictureDetails_viewPager.adapter = pictureDetailsAdapter

        pictureDetails_viewPager.pageSelections()
                .map { OnPictureChanged(it) }
                .bindToReactor()
    }

    override fun bindToState(stateObservable: Observable<PictureDetailsState>) {
        stateObservable.getChange { it.pictures }
                .observeState { pictureDetailsAdapter.data = it }
    }
}
