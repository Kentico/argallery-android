package com.sumera.argallery.ui.common

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView

class DynamicImageView(context: Context, attrs: AttributeSet) : ImageView(context, attrs) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (drawable != null) {
            val width = MeasureSpec.getSize(widthMeasureSpec)
            val height = Math.ceil((width * drawable.intrinsicHeight.toFloat() / drawable.intrinsicWidth).toDouble()).toInt()
            this.setMeasuredDimension(width, height)
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        }
    }
}