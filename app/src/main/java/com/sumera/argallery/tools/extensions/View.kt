package com.sumera.argallery.tools.extensions

import android.view.View

fun View.setVisibile(isVisible: Boolean) {
    this.visibility = if(isVisible) View.VISIBLE else View.GONE
}

fun View.isVisibile(): Boolean {
    return this.visibility == View.VISIBLE
}