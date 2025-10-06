package com.linkjf.spacex.launch.mvi

import app.cash.turbine.test
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

private sealed interface TestState {
    data object Empty : TestState

    data object Loading : TestState

    data class Value(
        val value: Int,
    ) : TestState
}

private sealed interface TestAction {
    data class Set(
        val value: Int,
    ) : TestAction

    data class Emit(
        val value: String,
    ) : TestAction

    data object SetEmpty : TestAction

    data object SetLoading : TestAction
}

private class TestViewModel(
    initial: TestState = TestState.Empty,
) : StateViewModel<TestState, String, TestAction>(initial) {
    override fun reduce(action: TestAction) {
        when (action) {
            is TestAction.Set -> TestState.Value(action.value).sendToState()
            is TestAction.Emit -> action.value.sendToEvent()
            TestAction.SetEmpty -> TestState.Empty.sendToState()
            TestAction.SetLoading -> TestState.Loading.sendToState()
        }
    }

    fun setSameAsCurrent() {
        state.value.sendToState()
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
class StateViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `initial state is exposed`() =
        runTest {
            val vm = TestViewModel(initial = TestState.Value(7))
            assertEquals(TestState.Value(7), vm.state.value)
        }

    @Test
    fun `sendToState updates state when value changes`() =
        runTest {
            val vm = TestViewModel(initial = TestState.Value(0))

            vm.state.test {
                assertEquals(TestState.Value(0), awaitItem()) // initial emission
                vm.reduce(TestAction.Set(1))
                assertEquals(TestState.Value(1), awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `sendToState does not emit when value is equal`() =
        runTest {
            val vm = TestViewModel(initial = TestState.Value(5))

            vm.state.test {
                assertEquals(TestState.Value(5), awaitItem())
                vm.setSameAsCurrent()
                expectNoEvents()
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `sendToEvent emits through event flow`() =
        runTest {
            val vm = TestViewModel()

            vm.event.test {
                vm.reduce(TestAction.Emit("HELLO"))
                assertEquals("HELLO", awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `currentState returns initial value`() =
        runTest {
            val vm = TestViewModel(initial = TestState.Value(99))
            assertEquals(TestState.Value(99), vm.currentState())
        }

    @Test
    fun `currentState returns updated value after Set`() =
        runTest {
            val vm = TestViewModel(initial = TestState.Value(0))
            vm.reduce(TestAction.Set(42))
            assertEquals(TestState.Value(42), vm.currentState())
        }

    @Test
    fun `currentState does not change after Emit`() =
        runTest {
            val vm = TestViewModel(initial = TestState.Value(7))
            vm.reduce(TestAction.Emit("noop"))
            assertEquals(TestState.Value(7), vm.currentState())
        }

    @Test
    fun `state transitions across shapes emit properly`() =
        runTest {
            val vm = TestViewModel(initial = TestState.Empty)

            vm.state.test {
                assertEquals(TestState.Empty, awaitItem())

                vm.reduce(TestAction.SetLoading)
                assertEquals(TestState.Loading, awaitItem())

                vm.reduce(TestAction.Set(3))
                assertEquals(TestState.Value(3), awaitItem())

                vm.reduce(TestAction.SetEmpty)
                assertEquals(TestState.Empty, awaitItem())

                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `setting equal Value does not emit again`() =
        runTest {
            val vm = TestViewModel(initial = TestState.Value(10))

            vm.state.test {
                assertEquals(TestState.Value(10), awaitItem())
                vm.reduce(TestAction.Set(10)) // structurally equal
                expectNoEvents()
                vm.reduce(TestAction.Set(11))
                assertEquals(TestState.Value(11), awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `currentState remains consistent across multiple reads`() =
        runTest {
            val vm = TestViewModel(initial = TestState.Value(1))
            assertEquals(TestState.Value(1), vm.currentState())

            vm.reduce(TestAction.Set(2))
            assertEquals(TestState.Value(2), vm.currentState())

            vm.reduce(TestAction.SetLoading)
            assertEquals(TestState.Loading, vm.currentState())

            vm.reduce(TestAction.SetEmpty)
            assertEquals(TestState.Empty, vm.currentState())
        }
}
