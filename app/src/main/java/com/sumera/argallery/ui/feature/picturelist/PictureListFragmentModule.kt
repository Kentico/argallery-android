package com.sumera.argallery.ui.feature.picturelist

import android.support.v4.app.Fragment
import com.sumera.argallery.injection.PerFragment
import dagger.Binds
import dagger.Module

@Module
abstract class PictureListFragmentModule {

    @Binds
    @PerFragment
    abstract fun fragment(pictureListFragment: PictureListFragment): Fragment
}