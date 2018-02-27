package com.sumera.argallery.injection.modules

import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import dagger.Module
import dagger.Provides

@Module
open class BaseFragmentModule {

    @Provides
    fun context(fragment: Fragment): AppCompatActivity {
        return fragment.activity as? AppCompatActivity ?: throw IllegalStateException()
    }
}
