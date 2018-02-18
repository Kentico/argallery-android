package com.sumera.argallery.ui.feature.picturelist.adapter.info

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.sumera.argallery.data.store.ui.model.Picture
import javax.inject.Inject

class PictureInfoAdapter @Inject constructor() : RecyclerView.Adapter<PictureInfoViewHolder>() {

    var pictures: List<Picture> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }


    override fun getItemCount(): Int {
        return pictures.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PictureInfoViewHolder {
        return PictureInfoViewHolder.createInstance(parent)
    }

    override fun onBindViewHolder(holder: PictureInfoViewHolder, position: Int) {
       holder.bind(pictures[position])
    }
}