package com.sumera.argallery.tools.log

import com.github.ajalt.timberkt.Timber
import com.sumera.argallery.data.store.ui.datasource.CurrentDataSourceStore
import javax.inject.Inject

class StateLogger @Inject constructor(
        private val currentDataSourceStore: CurrentDataSourceStore
) {

    fun init() {
        currentDataSourceStore.getPicturesWithLoadingStateObservable()
                .doOnDispose {  Timber.d { "CURRENT DATA SOURCE = DISPOSE" } }
                .doOnNext {
                    Timber.d { "CURRENT DATA SOURCE = $it" }
                }.subscribe()
    }

}
