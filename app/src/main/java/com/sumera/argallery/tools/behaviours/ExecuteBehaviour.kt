package com.sumera.argallery.tools.behaviours

import com.sumera.koreactor.behaviour.CompletableWorker
import com.sumera.koreactor.behaviour.MviBehaviour
import com.sumera.koreactor.behaviour.Triggers
import com.sumera.koreactor.reactor.data.MviReactorMessage
import com.sumera.koreactor.reactor.data.MviState
import io.reactivex.Observable

class ExecuteBehaviour<INPUT, STATE : MviState>(
        private val triggers: Triggers<out INPUT>,
        private val worker: CompletableWorker<INPUT>
) : MviBehaviour<STATE> {

    override fun createObservable(): Observable<out MviReactorMessage<STATE>> {
        return triggers.merge()
                .flatMap { worker.executeAsObservable(it) }
                .map { emptyMessage<STATE>() }
    }
}