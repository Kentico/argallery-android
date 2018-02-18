package com.sumera.argallery.tools.extensions

import android.content.Context
import android.support.v7.widget.RecyclerView

fun RecyclerView.ViewHolder.context(): Context {
    return itemView.context
}