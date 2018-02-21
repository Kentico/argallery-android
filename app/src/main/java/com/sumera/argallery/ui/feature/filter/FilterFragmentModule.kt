package com.sumera.argallery.ui.feature.filter

import android.support.v4.app.Fragment
import com.sumera.argallery.injection.PerActivity
import dagger.Binds
import dagger.Module

@Module
abstract class FilterFragmentModule {

    @Binds
    @PerActivity
    abstract fun fragment(filterFragment: FilterFragment): Fragment
}
