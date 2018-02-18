package com.sumera.argallery.tools.koreactor

import com.sumera.koreactor.behaviour.SingleWorker
import io.reactivex.Single

fun <INPUT, OUTPUT> action(action: (INPUT) -> OUTPUT): SingleWorker<INPUT, OUTPUT> {
    return SingleWorker({ Single.just(action(it)) })
}
