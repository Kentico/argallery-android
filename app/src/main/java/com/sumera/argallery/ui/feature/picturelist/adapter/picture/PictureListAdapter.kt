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
import io.reactivex.subjects.PublishSubject
import io.reactivex.Observable
import javax.inject.Inject

class PictureListAdapter @Inject constructor() : RecyclerView.Adapter<BasePictureListViewHolder>() {

    private val clickSubject = PublishSubject.create<Picture>()

    private var data = listOf<DataWrapper>()

    fun clicks(): Observable<Picture> {
        return clickSubject.hide()
    }

    fun setNewDataWithDiffUtil(newData: List<DataWrapper>, diffUtil: DiffUtil.DiffResult) {
        data = newData
        diffUtil.dispatchUpdatesTo(this)
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