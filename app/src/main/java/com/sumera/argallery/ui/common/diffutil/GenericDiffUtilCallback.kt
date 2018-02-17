package com.sumera.argallery.ui.common.diffutil

import android.support.v7.util.DiffUtil
import io.reactivex.Observable

class GenericDiffUtilCallback(
        var newData: List<DiffUtilItem>,
        var oldData: List<DiffUtilItem>
) : DiffUtil.Callback() {

    companion object {
        fun calculate(newData: List<DiffUtilItem>, oldData: List<DiffUtilItem>): Observable<DiffUtil.DiffResult> {
            return Observable.fromCallable {
                val callback = GenericDiffUtilCallback(newData, oldData)
                DiffUtil.calculateDiff(callback)
            }
        }
    }

    override fun getOldListSize(): Int {
        return oldData.size
    }

    override fun getNewListSize(): Int {
        return newData.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldData[oldItemPosition].diffUtilIdentity === newData[newItemPosition].diffUtilIdentity
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldData[oldItemPosition] == newData[newItemPosition]
    }
}