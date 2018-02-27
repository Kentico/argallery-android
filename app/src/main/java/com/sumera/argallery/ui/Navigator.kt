package com.sumera.argallery.ui

import android.support.v7.app.AppCompatActivity
import com.sumera.argallery.R
import com.sumera.argallery.injection.PerActivity
import com.sumera.argallery.ui.feature.filter.FilterFragment
import javax.inject.Inject

class Navigator @Inject constructor(
        private val activity: AppCompatActivity
) {

    fun navigateToFilter() {
        val fm = activity.supportFragmentManager
        fm.beginTransaction()
                .add(R.id.main_filterTabContainer, FilterFragment.newInstance(), "tag")
                .commit()
    }
}
