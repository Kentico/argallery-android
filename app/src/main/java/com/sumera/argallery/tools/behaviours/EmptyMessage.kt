package com.sumera.argallery.tools.behaviours

import com.sumera.koreactor.internal.data.EventOrReducer
import com.sumera.koreactor.reactor.data.MviReactorMessage
import com.sumera.koreactor.reactor.data.MviState

class EmptyMessage<STATE : MviState> : MviReactorMessage<STATE> {
    override fun messages(): Collection<EventOrReducer<STATE>> {
        return listOf()
    }
}

fun <STATE : MviState> emptyMessage(): EmptyMessage<STATE> {
    return EmptyMessage()
}