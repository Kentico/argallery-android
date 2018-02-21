package com.sumera.argallery.ui.feature.picturelist.adapter.picture

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.sumera.argallery.data.store.ui.model.Picture
import com.sumera.argallery.ui.common.diffutil.DiffUtilItem
import com.sumera.argallery.ui.feature.picturelist.adapter.picture.viewholder.BasePictureListViewHolder
import com.sumera.argallery.ui.feature.picturelist.adapter.picture.viewholder.ErrorViewHolder
import com.sumera.argallery.ui.feature.picturelist.adapter.picture.viewholder.LoadingViewHolder
import com.sumera.argallery.ui.feature.picturelist.adapter.picture.viewholder.PictureViewHolder
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.view_holder_picture_list_error.view.*
import javax.inject.Inject

class PictureListAdapter @Inject constructor() : RecyclerView.Adapter<BasePictureListViewHolder>() {

    private val errorClicksSubject = PublishSubject.create<Unit>()

    private val clickSubject = PublishSubject.create<Picture>()

    private val onListEndReached = PublishSubject.create<Unit>()

    private var data = listOf<DataWrapper>()

    fun errorClicks(): Observable<Unit> {
        return errorClicksSubject.hide()
    }

    fun clicks(): Observable<Picture> {
        return clickSubject.hide()
    }

    fun onListEndReached(): Observable<Unit> {
        return onListEndReached.hide()
    }

    fun setNewDataWithDiffUtil(newData: List<DataWrapper>, diffUtil: DiffUtil.DiffResult) {
        data = newData
        notifyDataSetChanged()
//        diffUtil.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BasePictureListViewHolder {
        return when(ViewHolderType.values()[viewType]) {
            ViewHolderType.PICTURE -> PictureViewHolder.createInstance(parent)
            ViewHolderType.LOADING -> LoadingViewHolder.createInstance(parent)
            ViewHolderType.ERROR -> ErrorViewHolder.createInstance(parent)
        }
    }

    override fun onBindViewHolder(holder: BasePictureListViewHolder?, position: Int) {
        when(holder) {
            is PictureViewHolder -> {
                data[position].picture?.let { picture ->
                    holder.itemView.setOnClickListener { clickSubject.onNext(picture) }
                    holder.bind(picture)
                } ?: throw IllegalStateException()
            }
            is ErrorViewHolder -> {
                holder.itemView.errorItem_tryAgain.setOnClickListener {
                    errorClicksSubject.onNext(Unit)
                }
            }
        }

        if (position >= itemCount - 2) {
            onListEndReached.onNext(Unit)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun getItemViewType(position: Int): Int {
        return data[position].type.ordinal
    }

    enum class ViewHolderType {
        PICTURE, LOADING, ERROR
    }

    data class DataWrapper(
            val picture: Picture?,
            val type: ViewHolderType
    ) : DiffUtilItem {
        override val diffUtilIdentity: String
            get() {
                return when(type) {
                    ViewHolderType.PICTURE -> picture?.diffUtilIdentity ?: throw IllegalStateException()
                    ViewHolderType.ERROR -> "error_identity"
                    ViewHolderType.LOADING -> "loading_identity"
                }
            }
    }
}