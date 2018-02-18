package com.sumera.argallery.ui.feature.picturelist.adapter.info

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.sumera.argallery.R
import com.sumera.argallery.data.store.ui.model.Picture
import com.sumera.argallery.tools.extensions.inflateViewHolder
import kotlinx.android.synthetic.main.view_holder_picture_info.view.*

class PictureInfoViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    companion object {
        fun createInstance(parent: ViewGroup) : PictureInfoViewHolder {
            val view = parent.inflateViewHolder(R.layout.view_holder_picture_info)
            return PictureInfoViewHolder(view)
        }
    }

    fun bind(picture: Picture) {
        itemView.pictureInfoItem_title.text = picture.title
    }
}