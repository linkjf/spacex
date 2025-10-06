package com.linkjf.spacex.launch.mvi

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.flow.Flow

@Composable
fun <T> EventEffect(
    flow: Flow<T>,
    onEvent: (T) -> Unit,
) {
    LaunchedEffect(key1 = flow) {
        flow.collect { onEvent(it) }
    }
}
