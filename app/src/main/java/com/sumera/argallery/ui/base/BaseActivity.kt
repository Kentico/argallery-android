package com.sumera.argallery.ui.base

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.Fragment
import com.sumera.koreactor.reactor.data.MviState
import com.sumera.koreactor.view.implementation.MviActivity
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

abstract class BaseActivity<STATE> : MviActivity<STATE>(), HasSupportFragmentInjector
        where STATE : MviState {

    @Inject lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>

    protected abstract val layoutRes: Int

    @SuppressLint("MissingSuperCall")
    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)

        super.onCreate(savedInstanceState)

        if (layoutRes != 0) {
            setContentView(layoutRes)
        }
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        return fragmentInjector
    }
}