package com.sumera.argallery.ui.feature.picturelist

import android.support.v4.app.Fragment
import com.sumera.argallery.injection.PerFragment
import com.sumera.argallery.injection.modules.BaseFragmentModule
import dagger.Binds
import dagger.Module

@Module(includes = [BaseFragmentModule::class])
abstract class PictureListFragmentModule {

    @Binds
    @PerFragment
    abstract fun fragment(pictureListFragment: PictureListFragment): Fragment
}