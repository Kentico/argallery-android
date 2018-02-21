package com.sumera.argallery.domain.filter

import com.sumera.argallery.data.store.ui.FilterStore
import com.sumera.argallery.data.store.ui.model.Filter
import com.sumera.argallery.domain.base.BaseCompletabler
import io.reactivex.Completable
import javax.inject.Inject

class SetCurrentFilterCompletabler @Inject constructor(
        private val filterStore: FilterStore
) : BaseCompletabler() {

    private lateinit var filter: Filter

    fun init(filter: Filter): SetCurrentFilterCompletabler {
        this.filter = filter
        return this
    }

    override fun create(): Completable {
        return Completable.fromCallable {
            filterStore.setCurrentFilter(filter)
        }
    }
}