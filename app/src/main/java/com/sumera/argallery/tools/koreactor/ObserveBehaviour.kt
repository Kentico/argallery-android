package com.sumera.argallery.tools.koreactor

import com.sumera.koreactor.behaviour.Messages
import com.sumera.koreactor.behaviour.MviBehaviour
import com.sumera.koreactor.behaviour.Triggers
import com.sumera.koreactor.behaviour.Worker
import com.sumera.koreactor.reactor.data.MviReactorMessage
import com.sumera.koreactor.reactor.data.MviState
import io.reactivex.Observable

class ObserveBehaviour<INPUT, OUTPUT, STATE : MviState>(
        private val triggers: Triggers<INPUT>,
        private val worker: Worker<INPUT, OUTPUT>,
        private val message: Messages<OUTPUT, STATE>
) : MviBehaviour<STATE> {

    override fun createObservable(): Observable<out MviReactorMessage<STATE>> {
        return triggers.merge()
                .switchMap { worker.executeAsObservable(it) }
                .map { message.applyData(it) }
    }
}