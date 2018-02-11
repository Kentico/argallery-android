package com.sumera.argallery.ui.base

import com.sumera.koreactor.reactor.MviReactor
import com.sumera.koreactor.reactor.data.MviState

abstract class BaseReactor<STATE : MviState> : MviReactor<STATE>()