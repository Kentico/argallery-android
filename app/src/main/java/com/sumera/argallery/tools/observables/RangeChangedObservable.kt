package com.sumera.argallery.tools.observables

import com.appyvet.materialrangebar.RangeBar
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.MainThreadDisposable

class RangeChangedObservable(private val view: RangeBar) : Observable<Pair<Int, Int>>() {

    override fun subscribeActual(observer: Observer<in Pair<Int, Int>>) {
        val listener = Listener(view, observer)
        observer.onSubscribe(listener)
        view.setOnRangeBarChangeListener(listener)
    }

    internal class Listener(private val view: RangeBar, private val observer: Observer<in Pair<Int, Int>>) : MainThreadDisposable(), RangeBar.OnRangeBarChangeListener {

        override fun onRangeChangeListener(rangeBar: RangeBar?, leftPinIndex: Int, rightPinIndex: Int, leftPinValue: String?, rightPinValue: String?) {
            val leftValue = leftPinValue?.toInt() ?: throw IllegalStateException("Unexpected value $leftPinValue")
            val rightValue = rightPinValue?.toInt() ?: throw IllegalStateException("Unexpected value $rightPinValue")
            observer.onNext(leftValue to rightValue)
        }

        override fun onDispose() {
            view.setOnRangeBarChangeListener(null)
        }
    }
}

fun RangeBar.rangeChanges(): Observable<Pair<Int, Int>> {
    return RangeChangedObservable(this)
}