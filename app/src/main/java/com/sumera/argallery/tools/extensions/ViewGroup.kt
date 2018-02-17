package com.sumera.argallery.tools.extensions

import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sumera.argallery.R

fun ViewGroup.inflateViewHolder(@LayoutRes resource: Int): View {
    return LayoutInflater.from(context).inflate(R.layout.view_holder_picture_list_loading, this, false)
}