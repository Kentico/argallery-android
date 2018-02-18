package com.sumera.argallery.tools.extensions

import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

fun ViewGroup.inflateViewHolder(@LayoutRes resource: Int): View {
    return LayoutInflater.from(context).inflate(resource, this, false)
}