package com.sumera.argallery.ui.feature.picturelist.adapter.picture.viewholder

import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
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

    private var lastImageUrl = ""

    fun bind(picture: Picture) {
        if (lastImageUrl == picture.imageUrl) {
            return
        }

        lastImageUrl = picture.imageUrl

        Glide.with(context())
                .load(picture.imageUrl)
                .transition(withCrossFade())
                .into(itemView.pictureListItem_image)

        // Hack to resolve issue with incorrect image size after reuse of viewholder with different image
//        itemView.pictureListItem_image.layout(0, 0, 0, 0)
    }
}