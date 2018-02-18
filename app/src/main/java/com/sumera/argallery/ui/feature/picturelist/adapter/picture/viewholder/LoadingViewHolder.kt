package com.sumera.argallery.ui.feature.picturelist.adapter.picture.viewholder

import android.view.View
import android.view.ViewGroup
import com.sumera.argallery.R
import com.sumera.argallery.tools.extensions.inflateViewHolder

class LoadingViewHolder(view: View) : BasePictureListViewHolder(view) {

    companion object {
        fun createInstance(parent: ViewGroup) : LoadingViewHolder {
            val view = parent.inflateViewHolder(R.layout.view_holder_picture_list_loading)
            return LoadingViewHolder(view)
        }
    }
}