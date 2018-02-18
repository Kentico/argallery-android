package com.sumera.argallery.ui.feature.picturedetails.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.sumera.argallery.data.store.ui.model.Picture
import com.sumera.argallery.ui.feature.picturedetail.PictureDetailFragment
import javax.inject.Inject

class PictureDetailsAdapter @Inject constructor(
        private val fragmentManager: FragmentManager
) : FragmentStatePagerAdapter(fragmentManager) {

    var data: List<Picture> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItem(position: Int): Fragment {
        return PictureDetailFragment.newInstance(data[position])
    }

    override fun getCount(): Int {
        return data.size
    }
}