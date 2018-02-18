package com.sumera.argallery.ui.feature.picturelist.adapter.picture.viewholder

import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.sumera.argallery.R
import com.sumera.argallery.data.store.ui.model.Picture
import com.sumera.argallery.tools.extensions.context
import com.sumera.argallery.tools.extensions.inflateViewHolder
import kotlinx.android.synthetic.main.view_holder_picture_list_picture.view.*

class PictureViewHolder(view: View) : BasePictureListViewHolder(view) {

    companion object {
        fun createInstance(parent: ViewGroup) : PictureViewHolder {
            val view = parent.inflateViewHolder(R.layout.view_holder_picture_list_picture)
            return PictureViewHolder(view)
        }
    }

    fun bind(picture: Picture) {
        Glide.with(context())
                .load(picture.imageUrl)
                .into(itemView.pictureListItem_image)
    }
}