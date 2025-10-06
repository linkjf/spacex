package com.linkjf.spacex.launch.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Abstract class that models states, events and actions for all ViewModels.
 * Each ViewModel should follow this pattern to increase code consistency and reduce repetition.
 * NOTE: Don't modify this class by adding more functionality, this one is just for state management.
 *
 * S = State from VM to UI, represents the current data that the view must show
 * E = Event form VM to UI, non related with the UI state (effects)
 * A = Actions : user interaction from the UI to the ViewModel
 */

abstract class StateViewModel<S : Any, E : Any, A : Any>(
    initialState: S,
) : ViewModel() {
    // UI state of the Composable Screen
    private val mutableState = MutableStateFlow(initialState)
    val state: StateFlow<S> = mutableState.asStateFlow()

    inline fun <reified T : S> currentState(): T? = state.value as? T

    // Events to trigger to the Composables
    private val mutableEvent = MutableSharedFlow<E>(replay = 0)
    val event: SharedFlow<E> = mutableEvent.asSharedFlow()

    /**
     * Sets the state object as the current state.
     * If the new state is the same as the current state, the function returns without making any changes.
     *
     * @receiver The state object of type [S] to be set as the current state.
     */
    fun S.sendToState() {
        if (this == mutableState.value) return

        mutableState.value = this
    }

    /**
     * Emits the event to be consumed by the Composable.
     * It launches a coroutine in the [viewModelScope] to emit the event asynchronously.
     *
     * @receiver The event object of type [E] to be sent.
     */
    fun E.sendToEvent() {
        val thisEvent = this
        viewModelScope.launch {
            mutableEvent.emit(thisEvent)
        }
    }

    abstract fun reduce(action: A)
}
