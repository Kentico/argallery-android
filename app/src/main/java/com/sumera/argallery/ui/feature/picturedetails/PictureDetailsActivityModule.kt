package com.sumera.argallery.ui.feature.picturedetails

import android.app.Activity
import android.support.v4.app.FragmentManager
import com.sumera.argallery.data.store.ui.model.Picture
import com.sumera.argallery.injection.PerActivity
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module(includes = [PictureDetailsActivityModule.PictureDetailsActivityDataModule::class])
abstract class PictureDetailsActivityModule {

    @Module
    class PictureDetailsActivityDataModule {

        @Provides
        fun fragmentManager(activity: PictureDetailsActivity): FragmentManager {
            return activity.supportFragmentManager
        }

        @Provides
        fun picture(activity: PictureDetailsActivity): Picture {
            return activity.intent.getParcelableExtra(PictureDetailsActivity.pictureKey)
        }
    }

    @Binds
    @PerActivity
    abstract fun fragment(pictureDetailsActivity: PictureDetailsActivity): Activity
}