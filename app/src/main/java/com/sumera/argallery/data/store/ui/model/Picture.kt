package com.sumera.argallery.data.store.ui.model

import com.sumera.argallery.ui.common.diffutil.DiffUtilItem

data class Picture(
        val id: String,
        val title: String,
        val author: String,
        val description: String,
        val imageUrl: String,
        val price: Int
) : DiffUtilItem {

    override val diffUtilIdentity = id
}