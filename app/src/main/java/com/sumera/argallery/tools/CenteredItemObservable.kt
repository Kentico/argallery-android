package com.sumera.argallery.tools

import android.support.v7.widget.RecyclerView
import com.yarolegovich.discretescrollview.DiscreteScrollView
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.MainThreadDisposable

class CenteredItemObservable(private val view: DiscreteScrollView) : Observable<Int>() {

    override fun subscribeActual(observer: Observer<in Int>) {
        val listener = Listener(view, observer)
        observer.onSubscribe(listener)
        view.addScrollListener(listener)
    }

    internal class Listener(private val view: DiscreteScrollView, private val observer: Observer<in Int>) : MainThreadDisposable(), DiscreteScrollView.ScrollListener<RecyclerView.ViewHolder> {

        override fun onScroll(scrollPosition: Float, currentPosition: Int, newPosition: Int, currentHolder: RecyclerView.ViewHolder?, newCurrent: RecyclerView.ViewHolder?) {
            if (Math.abs(scrollPosition) > 0.5) {
                observer.onNext(newPosition)
            } else {
                observer.onNext(currentPosition)
            }
        }

        override fun onDispose() {
            view.removeScrollListener(this)
        }
    }
}

fun DiscreteScrollView.centerItemPositions(): Observable<Int> {
    return CenteredItemObservable(this)
}