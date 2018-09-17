package com.sumera.argallery.data.store.ui.model

import android.os.Parcelable
import com.sumera.argallery.ui.common.diffutil.DiffUtilItem
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Picture(
        val id: String,
        val title: String,
        val author: String,
        val description: String,
        val imageUrl: String,
        val price: Int
) : DiffUtilItem, Parcelable {

    @Transient
    override val diffUtilIdentity = id
}