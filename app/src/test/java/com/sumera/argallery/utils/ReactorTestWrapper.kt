package com.sumera.koreactorexampleapp.lib

import com.sumera.koreactor.reactor.MviReactor
import com.sumera.koreactor.reactor.data.CreateState
import com.sumera.koreactor.reactor.data.DestroyState
import com.sumera.koreactor.reactor.data.DetachState
import com.sumera.koreactor.reactor.data.LifecycleState
import com.sumera.koreactor.reactor.data.MviAction
import com.sumera.koreactor.reactor.data.MviEvent
import com.sumera.koreactor.reactor.data.MviState
import com.sumera.koreactor.reactor.data.PauseState
import com.sumera.koreactor.reactor.data.ResumeState
import com.sumera.koreactor.reactor.data.StartState
import com.sumera.koreactor.reactor.data.StopState
import io.reactivex.schedulers.TestScheduler

class ReactorTestWrapper<STATE : MviState>(
		private val reactor: MviReactor<STATE>,
		private val testView: TestMviBindableDelegate<STATE>,
		private val testScheduler: TestScheduler
) {

	val scheduler: TestScheduler
		get() = testScheduler

	private var lastReactorLifecycleState: LifecycleState? = null

	//region From OnCreate lifecycle methods

	fun fromUninitializedToOnCreate() {
		checkOnCreateState(isNewlyCreated = true)

		callOnCreate(isNewlyCreated = true)
	}

	fun fromUninitializedToOnDestroy(isFinishing: Boolean) {
		checkOnCreateState(isNewlyCreated = true)

		callOnCreate(isNewlyCreated = true)
		callOnDestroy(isFinishing)
	}

	fun fromUninitializedToOnResume() {
		checkOnCreateState(isNewlyCreated = true)

		callOnCreate(isNewlyCreated = true)
		callOnStart()
		callOnResume()
	}

	fun fromUninitializedToOnStart() {
		checkOnCreateState(isNewlyCreated = true)

		callOnCreate(isNewlyCreated = true)
		callOnStart()
	}

	fun fromOnCreateToOnDestroy(isFinishing: Boolean = false) {
		checkLifecycleState(CreateState)

		callOnDestroy(isFinishing)
	}

	fun fromOnCreateToOnResume() {
		checkLifecycleState(CreateState)

		callOnStart()
		callOnResume()
	}

	fun fromOnCreateToOnStart() {
		checkLifecycleState(CreateState)

		callOnStart()
	}

	fun fromOnStartToOnResume() {
		checkLifecycleState(StartState)

		callOnResume()
	}

	fun fromOnResumeToOnPause() {
		checkLifecycleState(ResumeState)

		callOnPause()
	}

	fun fromOnResumeToOnStop() {
		checkLifecycleState(ResumeState)

		callOnPause()
		callOnStop()
	}

	fun fromOnResumeToOnDestroy(isFinishing: Boolean = false) {
		checkLifecycleState(ResumeState)

		callOnPause()
		callOnStop()
		callOnDestroy(isFinishing)
	}

	fun fromOnPauseToOnResume() {
		checkLifecycleState(PauseState)

		callOnResume()
	}

	fun fromOnPauseToOnStop() {
		checkLifecycleState(PauseState)

		callOnStop()
	}

	fun fromOnPauseToOnDestroy(isFinishing: Boolean = false) {
		checkLifecycleState(PauseState)

		callOnStop()
		callOnDestroy(isFinishing)
	}

	fun fromOnStopToOnStart() {
		checkLifecycleState(StopState)

		callOnStart()
	}

	fun fromOnStopToOnResume() {
		checkLifecycleState(StopState)

		callOnStart()
		callOnResume()
	}

	fun fromOnStopToOnDestroy(isFinishing: Boolean = false) {
		checkLifecycleState(StopState)

		callOnDestroy(isFinishing)
	}

	fun fromOnDestroyToOnCreate() {
		checkLifecycleState(DestroyState)

		callOnCreate(false)
	}

	fun fromOnDestroyToOnStart() {
		checkLifecycleState(DestroyState)

		callOnCreate(false)
		callOnStart()
	}

	fun fromOnDestroyToOnResume() {
		checkLifecycleState(DestroyState)

		callOnCreate(false)
		callOnStart()
		callOnResume()
	}

	//endregion

	//region Reactor lifecycle calls

	private fun callOnCreate(isNewlyCreated: Boolean) {
		reactor.setBindableView(testView)
		testScheduler.triggerActions()

		reactor.onCreate(isNewlyCreated)
		testScheduler.triggerActions()

		lastReactorLifecycleState = CreateState
	}

	private fun callOnStart() {
		reactor.onStart()
		testScheduler.triggerActions()

		lastReactorLifecycleState = StartState
	}

	private fun callOnResume() {
		reactor.onResume()
		testScheduler.triggerActions()

		lastReactorLifecycleState = ResumeState
	}

	private fun callOnPause() {
		reactor.onPause()
		testScheduler.triggerActions()

		lastReactorLifecycleState = PauseState
	}

	private fun callOnStop() {
		reactor.onStop()
		testScheduler.triggerActions()

		lastReactorLifecycleState = StopState
	}

	private fun callOnDestroy(isFinishing: Boolean = false) {
		reactor.onDestroy(isFinishing)
		testScheduler.triggerActions()

		lastReactorLifecycleState = if (isFinishing) DetachState else DestroyState
	}

	//region Send actions

	fun sendAction(action: MviAction<STATE>) {
		reactor.sendAction(action)

		testScheduler.triggerActions()
	}

	fun sendAction(actionFactory: () -> MviAction<STATE>) {
		sendAction(actionFactory())
	}

	fun sendActions(vararg actions: MviAction<STATE>) {
		actions.forEach { sendAction(it) }
	}

	fun trigger(block: () -> Unit) {
		block()

		scheduler.triggerActions()
	}

	//endregion

	//region Assertions

	fun assertNextState(expectedState: STATE) {
		testView.assertNextState(expectedState, lastReactorLifecycleState)
	}

	fun assertNextState(expectedStateFactory: () -> STATE) {
		assertNextState(expectedStateFactory())
	}

	fun assertNextStates(vararg expectedStates: STATE) {
		expectedStates.forEach { assertNextState(it) }
	}

	fun assertLastState(expectedState: STATE) {
		testView.assertLastState(expectedState, lastReactorLifecycleState)
	}

	fun assertLastState(expectedStateFactory: () -> STATE) {
		assertLastState(expectedStateFactory())
	}

	fun assertStatesCount(expectedCount: Int) {
		testView.assertStatesCount(expectedCount)
	}

	fun assertNoMoreStates() {
		testView.assertNoMoreStates()
	}

	fun assertNextEvent(expectedEvent: MviEvent<STATE>) {
		testView.assertNextEvent(expectedEvent)
	}

	fun assertNextEvent(expectedEventFactory: () -> MviEvent<STATE>) {
		assertNextEvent(expectedEventFactory())
	}

	fun assertNextEvents(vararg expectedEvents: MviEvent<STATE>) {
		expectedEvents.forEach { assertNextEvent(it) }
	}

	fun assertLastEvent(expectedEvent: MviEvent<STATE>) {
		testView.assertLastEvent(expectedEvent)
	}

	fun assertLastEvent(expectedEventFactory: () -> MviEvent<STATE>) {
		assertLastEvent(expectedEventFactory())
	}

	fun assertNoMoreEvents() {
		testView.assertNoMoreEvents()
	}

	fun assertEventsCount(expectedCount: Int) {
		testView.assertEventsCount(expectedCount)
	}

	fun assertNoMoreInteractions() {
		assertNoMoreEvents()
		assertNoMoreStates()
	}

	fun assertFinalReactorState() {
		testView.assertFinalState()
	}

	//endregion

	//region Test case wrappers

	fun runTestCaseWithoutAnyLifecycleCalls(testBlock: () -> Unit) {
		testBlock()

		performLifecycleEventsToUnbindReactor()
	}

	fun runTestCaseAfterOnCreate(testBlock: () -> Unit) {
		fromUninitializedToOnCreate()

		testBlock()

		performLifecycleEventsToUnbindReactor()
	}

	fun runTestCaseAfterOnStart(testBlock: () -> Unit) {
		fromUninitializedToOnStart()

		testBlock()

		performLifecycleEventsToUnbindReactor()
	}

	fun runTestCaseAfterOnResume(testBlock: () -> Unit) {
		fromUninitializedToOnResume()

		testBlock()

		performLifecycleEventsToUnbindReactor()
	}

	private fun performLifecycleEventsToUnbindReactor() {
		when (lastReactorLifecycleState) {
			is CreateState -> {
				fromOnCreateToOnDestroy(true)
			}
			is StartState -> {
				fromOnStartToOnResume()
				fromOnResumeToOnDestroy(true)
			}
			is ResumeState -> {
				fromOnResumeToOnDestroy(true)
			}
			is PauseState -> {
				fromOnPauseToOnDestroy(true)
			}
			is StopState -> {
				fromOnStopToOnDestroy(true)
			}
			is DestroyState -> {
				fromOnCreateToOnDestroy(true)
			}
		}
	}

	//endregion

	//region Helper methods

	private fun checkOnCreateState(isNewlyCreated: Boolean) {
		if (isNewlyCreated && lastReactorLifecycleState != null) {
			throw IllegalStateException("onCreate was already called with isNewlyCreated set to true")
		}
		if (!isNewlyCreated) {
			checkLifecycleState(DestroyState)
		}
	}

	private fun checkLifecycleState(expectedState: LifecycleState) {
		if (expectedState != lastReactorLifecycleState) {
			throw IllegalStateException("Last lifecycle state of reactor: ${lastReactorLifecycleState} Expected lifecycle state is ${expectedState}")
		}
	}
}