package com.sumera.argallery.domain.filter

import com.sumera.argallery.data.store.ui.FilterStore
import com.sumera.argallery.data.store.ui.model.Filter
import com.sumera.argallery.domain.base.BaseSingler
import io.reactivex.Single
import javax.inject.Inject

class GetCurrentFilterSingler @Inject constructor(
        private val filterStore: FilterStore
) : BaseSingler<Filter>() {

    override fun create(): Single<Filter> {
        return filterStore.getCurrentFilterSingle()
    }
}