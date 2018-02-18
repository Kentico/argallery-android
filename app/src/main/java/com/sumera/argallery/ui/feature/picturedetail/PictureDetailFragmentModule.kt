package com.sumera.argallery.ui.feature.picturedetail

import android.support.v4.app.Fragment
import com.sumera.argallery.data.store.ui.model.Picture
import com.sumera.argallery.injection.PerFragment
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module(includes = [PictureDetailFragmentModule.MovieDetailFragmentDataDataModule::class])
abstract class PictureDetailFragmentModule {

    @Module
    class MovieDetailFragmentDataDataModule {

        @Provides
        fun provideMovieFromIntent(fragment: PictureDetailFragment): Picture {
            return fragment.arguments?.getParcelable(PictureDetailFragment.pictureKey) ?: throw IllegalStateException()
        }
    }

    @Binds
    @PerFragment
    abstract fun fragment(pictureDetailFragment: PictureDetailFragment): Fragment
}