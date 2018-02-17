package com.sumera.argallery.ui.feature.picturelist

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.sumera.argallery.R
import com.sumera.argallery.tools.extensions.calculateDiffUtilResult
import com.sumera.argallery.ui.base.BaseFragment
import com.sumera.argallery.ui.feature.main.contract.PictureListState
import com.sumera.argallery.ui.feature.picturelist.adapter.PictureListAdapter
import com.sumera.koreactor.reactor.MviReactor
import com.sumera.koreactor.util.extension.getChange
import io.reactivex.Observable
import kotlinx.android.synthetic.main.fragment_pictures_with_info.*
import javax.inject.Inject

class PictureListFragment : BaseFragment<PictureListState>() {

    @Inject lateinit var reactorFactory: PictureListReactorFactory
    @Inject lateinit var adapter: PictureListAdapter

    override val layoutRes = R.layout.fragment_pictures_with_info

    override fun createReactor(): MviReactor<PictureListState> {
       return getReactor(reactorFactory, PictureListReactor::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pictureList_imageRecycler.layoutManager = LinearLayoutManager(context)
        pictureList_imageRecycler.adapter = adapter
    }

    override fun bindToState(stateObservable: Observable<PictureListState>) {
        stateObservable
                .getChange { it.pictures }
                .calculateDiffUtilResult()
                .observeState { (newData, diffResult) ->
                    adapter.setNewDataWithDiffUtil(newData, diffResult)
                }

    }
}
