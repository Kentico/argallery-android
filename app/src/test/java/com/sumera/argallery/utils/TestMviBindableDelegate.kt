package com.sumera.koreactorexampleapp.lib

import com.sumera.koreactor.internal.extension.isViewStarted
import com.sumera.koreactor.reactor.data.LifecycleState
import com.sumera.koreactor.reactor.data.MviEvent
import com.sumera.koreactor.reactor.data.MviState
import com.sumera.koreactor.view.MviBindableViewDelegate
import io.reactivex.Observable
import io.reactivex.observers.TestObserver
import io.reactivex.subjects.PublishSubject

class TestMviBindableDelegate<STATE : MviState> : MviBindableViewDelegate<STATE> {

	companion object {
		const val INVALID_VALUE = -1
	}

	private var assertedValuesCounter = 0

	private var assertedEventsCounter = 0

	private val stateTestObserver = TestObserver<STATE>()

	private val stateSubject = PublishSubject.create<STATE>()

	private var expectedStateCount = INVALID_VALUE

	private var eventsTestObserver = TestObserver<MviEvent<STATE>>()

	private var eventsSubject =  PublishSubject.create<MviEvent<STATE>>()

	private var expectedEventsCount = INVALID_VALUE

	init {
		stateSubject.subscribe(stateTestObserver)
		eventsSubject.subscribe(eventsTestObserver)
	}

	//region MviBindable implementation

	override fun bindToState(stateObservable: Observable<STATE>) {
		stateObservable.subscribe(stateSubject)
	}

	override fun bindToEvent(eventsObservable: Observable<MviEvent<STATE>>) {
		eventsObservable.subscribe(eventsSubject)
	}

	override fun unbindFromState() {
		// Do nothing
	}

	override fun unbindFromEvents() {
		// Do nothing
	}

	override fun unbindActions() {
		// Do nothing
	}

	//endregion

	//region State assertions

	fun assertNextState(expectedState: STATE, currentLifecycleState: LifecycleState?) {
		if (assertedValuesCounter > stateTestObserver.valueCount() - 1) {
			if (currentLifecycleState?.isViewStarted() == true) {
				throw AssertionError("No new state.")
			}
			if (currentLifecycleState == null) {
				throw AssertionError("No new state. Reactor is receiving state updates only between onStart and onStop calls. No lifecycle method was called.")
			}
			throw AssertionError("No new state. Reactor is receiving state updates only between onStart and onStop calls. Current lifecycle state of your reactor is $currentLifecycleState.")
		}
		stateTestObserver.assertValueAt(assertedValuesCounter, expectedState)
		assertedValuesCounter++
	}

	fun assertLastState(expectedState: STATE, currentLifecycleState: LifecycleState?) {
		if (stateTestObserver.valueCount() == 0) {
			if (currentLifecycleState == null) {
				throw AssertionError("No state received. Reactor is receiving state updates only between onStart and onStop calls. No lifecycle method was called.")
			}
			throw AssertionError("No state received. Reactor is receiving state updates only between onStart and onStop calls. Current lifecycle state of your reactor is $currentLifecycleState.")
		}
		val lastIndex = stateTestObserver.valueCount() - 1
		stateTestObserver.assertValueAt(lastIndex, expectedState)
	}

	fun assertNoMoreStates() {
		expectedStateCount = stateTestObserver.valueCount()
	}

	fun assertStatesCount(expectedCount: Int) {
		stateTestObserver.assertValueCount(expectedCount)
	}

	//endregion

	//region Events assertions

	fun assertNextEvent(expectedEvent: MviEvent<STATE>) {
		if (assertedEventsCounter > eventsTestObserver.valueCount() - 1) {
			throw AssertionError("No new event. Make sure what kind of MviEventBehaviour has your event implemented")
		}
		eventsTestObserver.assertValueAt(assertedEventsCounter, expectedEvent)
		assertedEventsCounter++
	}

	fun assertLastEvent(expectedEvent: MviEvent<STATE>) {
		if (eventsTestObserver.valueCount() == 0) {
			throw AssertionError("No event received. Make sure what kind of MviEventBehaviour has your event implemented\"")
		}
		val lastIndex = eventsTestObserver.valueCount() - 1
		eventsTestObserver.assertValueAt(lastIndex, expectedEvent)
	}

	fun assertNoMoreEvents() {
		expectedEventsCount = eventsTestObserver.valueCount()
	}

	fun assertEventsCount(expectedCount: Int) {
		eventsTestObserver.assertValueCount(expectedCount)
	}

	//endregion

	fun assertFinalState() {
		if (expectedStateCount != INVALID_VALUE && expectedStateCount != stateTestObserver.valueCount()) {
			throw AssertionError("Reactor emitted another unexpected states")
		}
		if (expectedEventsCount != INVALID_VALUE && expectedEventsCount != eventsTestObserver.valueCount()) {
			throw AssertionError("Reactor emitted another unexpected events")
		}
		if (eventsTestObserver.errorCount() > 0) {
			throw KoreactorInternalException("event observable with errors: ${eventsTestObserver.errors()}")
		}
		if (stateTestObserver.errorCount() > 0) {
			throw KoreactorInternalException("state observable with errors: ${stateTestObserver.errors()}")
		}
	}
}
