package com.sumera.argallery.data.store.persistence

import com.sumera.argallery.data.mapper.PictureMapper
import com.sumera.argallery.data.store.ui.datasource.AbstractDataSource
import com.sumera.argallery.data.store.ui.datasource.model.DataSourceType
import com.sumera.argallery.data.store.ui.datasource.model.LoadingState
import com.sumera.argallery.data.store.ui.model.PicturesWithLoadingState
import com.sumera.argallery.tools.log.ErrorLogger
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavouritePicturesDataSourceStore @Inject constructor(
        private val appDatabase: AppDatabase,
        private val mapper: PictureMapper,
        private val errorLogger: ErrorLogger
) : AbstractDataSource() {

    override val dataSourceType = DataSourceType.FAVOURITES

    init {
        subscribeToDatabase()
    }

    override fun loadMore() {
        // Not required
    }

    override fun reload() {
        // Not required
    }

    private fun subscribeToDatabase() {
        appDatabase.favouritePicturesDao().getAll()
                .map { mapper.toPictures(it) }
                .map { PicturesWithLoadingState(pictures = it, loadingState = LoadingState.COMPLETED)}
                .subscribeOn(Schedulers.io())
                .subscribe { picturesWithLoadingState ->
                    changeState(picturesWithLoadingState)
                }
    }
}
