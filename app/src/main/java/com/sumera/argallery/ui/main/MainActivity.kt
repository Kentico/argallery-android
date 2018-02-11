package com.sumera.argallery.ui.main

import android.os.Bundle
import com.github.ajalt.timberkt.Timber
import com.sumera.argallery.R
import com.sumera.argallery.ui.base.BaseActivity
import com.sumera.argallery.ui.main.contract.MainState
import com.sumera.koreactor.reactor.MviReactor
import javax.inject.Inject

class MainActivity : BaseActivity<MainState>() {

    @Inject lateinit var reactorFactory: MainReactorFactory

    override val layoutRes = R.layout.activity_main

    override fun createReactor(): MviReactor<MainState> {
       return getReactor(reactorFactory, MainReactor::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Timber.d { "ds" }
    }
}
