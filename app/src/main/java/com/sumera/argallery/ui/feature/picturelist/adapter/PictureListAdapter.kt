package com.sumera.argallery.ui.feature.picturelist.adapter

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.sumera.argallery.data.store.ui.model.Picture
import com.sumera.argallery.ui.feature.picturelist.adapter.viewholder.BasePictureListViewHolder
import com.sumera.argallery.ui.feature.picturelist.adapter.viewholder.LoadingViewHolder
import javax.inject.Inject

class PictureListAdapter @Inject constructor() : RecyclerView.Adapter<BasePictureListViewHolder>() {

    private var data = listOf<Picture>()

    fun setNewDataWithDiffUtil(newData: List<Picture>, diffUtil: DiffUtil.DiffResult) {
        data = newData
        diffUtil.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BasePictureListViewHolder {
        return LoadingViewHolder.createInstance(parent)
    }

    override fun onBindViewHolder(holder: BasePictureListViewHolder?, position: Int) {

    }

    override fun getItemCount(): Int {
        return data.size
    }

    enum class ViewHolderType {
        LOADING
    }
}