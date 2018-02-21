package com.sumera.argallery.injection.modules

import com.sumera.argallery.injection.PerFragment
import com.sumera.argallery.ui.feature.filter.FilterFragment
import com.sumera.argallery.ui.feature.filter.FilterFragmentModule
import com.sumera.argallery.ui.feature.picturedetail.PictureDetailFragment
import com.sumera.argallery.ui.feature.picturedetail.PictureDetailFragmentModule
import com.sumera.argallery.ui.feature.picturelist.PictureListFragment
import com.sumera.argallery.ui.feature.picturelist.PictureListFragmentModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBuilderModule {

    @PerFragment
    @ContributesAndroidInjector(modules = [PictureListFragmentModule::class])
    internal abstract fun pictureListFragment(): PictureListFragment

    @PerFragment
    @ContributesAndroidInjector(modules = [PictureDetailFragmentModule::class])
    internal abstract fun pictureDetailFragment(): PictureDetailFragment

    @PerFragment
    @ContributesAndroidInjector(modules = [FilterFragmentModule::class])
    abstract fun filterFragment(): FilterFragment
}
