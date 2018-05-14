package com.sumera.argallery.data.store.remote

import com.sumera.argallery.data.store.ui.datasource.AbstractDataSource
import com.sumera.argallery.data.store.ui.datasource.model.DataSourceType
import com.sumera.argallery.data.store.ui.datasource.model.LoadingState
import com.sumera.argallery.data.store.ui.model.Picture
import com.sumera.argallery.data.store.ui.model.PicturesWithLoadingState
import com.sumera.argallery.tools.DATA_REQUEST_LIMIT
import com.sumera.argallery.tools.log.ErrorLogger
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class AllPicturesDataSourceStore @Inject constructor(
        private val kenticoStore: KenticoStore,
        private val errorLogger: ErrorLogger
) : AbstractDataSource() {

    override val dataSourceType = DataSourceType.ALL

    private val startLoadingSubject by lazy { PublishSubject.create<LoadingType>() }

    init {
        subscribeToAllPictures()
    }

    override fun loadMore() {
        startLoadingSubject.onNext(LoadingType.LOAD_MORE)
    }

    override fun reload() {
        startLoadingSubject.onNext(LoadingType.RELOAD)
    }

    open protected fun createQueryParams(): Map<String, String> {
        return emptyMap()
    }

    private fun subscribeToAllPictures() {
        Observable.merge(startLoadingSubject, Observable.just(LoadingType.RELOAD))
                .switchMapSingle { loadingType -> getPreviousData(loadingType) }
                .switchMap { previousPictures -> loadPictures(previousPictures) }
                .subscribeOn(Schedulers.io())
                .subscribe { picturesWithLoadingState ->
                    changeState(picturesWithLoadingState)
                }
    }

    private fun loadPictures(previousPictures: List<Picture>): Observable<PicturesWithLoadingState> {
        return kenticoStore.getPictures(DATA_REQUEST_LIMIT, previousPictures.size, createQueryParams()).toObservable()
                .map { newPictures ->
                    val state = if (newPictures.size < DATA_REQUEST_LIMIT) {
                        LoadingState.COMPLETED
                    } else {
                        LoadingState.INACTIVE
                    }
                    PicturesWithLoadingState(pictures = previousPictures + newPictures, loadingState = state)
                }
                .doOnError { errorLogger.logException(it) }
                .onErrorReturn { PicturesWithLoadingState(pictures = previousPictures, loadingState = LoadingState.ERROR) }
                .startWith(PicturesWithLoadingState(pictures = previousPictures, loadingState = LoadingState.LOADING))
    }

    private fun getPreviousData(loadingType: LoadingType): Single<List<Picture>> {
        return if (loadingType == LoadingType.RELOAD) {
            Single.just(listOf())
        } else {
            picturesWithLoadingStateSingle.map { it.pictures }
        }
    }

    enum class LoadingType {
        RELOAD, LOAD_MORE
    }

}
