package com.linkjf.spacex.launch.home.presentation

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.linkjf.spacex.launch.designsystem.components.LaunchListItem
import com.linkjf.spacex.launch.home.domain.usecase.GetPastLaunchesUseCase
import com.linkjf.spacex.launch.home.domain.usecase.GetUpcomingLaunchesUseCase
import com.linkjf.spacex.launch.mvi.StateViewModel
import com.linkjf.spacex.launch.network.RateLimitInterceptor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
    @Inject
    constructor(
        private val getUpcomingLaunches: GetUpcomingLaunchesUseCase,
        private val getPastLaunches: GetPastLaunchesUseCase,
        private val rateLimitInterceptor: RateLimitInterceptor,
    ) : StateViewModel<HomeState, HomeEvent, HomeAction>(HomeState()) {
        val upcomingLaunches: Flow<PagingData<LaunchListItem>> =
            getUpcomingLaunches().cachedIn(viewModelScope)
        val pastLaunches: Flow<PagingData<LaunchListItem>> =
            getPastLaunches().cachedIn(viewModelScope)

        init {
            checkRateLimitStatus()
            startRateLimitCountdown()
        }

        companion object {
            private const val COUNTDOWN_UPDATE_INTERVAL_MS = 1000L
        }

        override fun reduce(action: HomeAction) {
            when (action) {
                HomeAction.TapSettings -> HomeEvent.NavigateToSettings.sendToEvent()

                is HomeAction.SelectFilter -> selectFilter(action.filter)
                is HomeAction.SelectTab -> selectTab(action.tabIndex)

                is HomeAction.TapLaunch ->
                    HomeEvent
                        .NavigateToLaunchDetails(action.launchId)
                        .sendToEvent()

                is HomeAction.TapWatch -> handleWatchAction(action.launchId)

                HomeAction.DismissError -> dismissError()
                HomeAction.DismissRateLimitError -> dismissRateLimitError()
            }
        }

        private fun selectTab(tabIndex: Int) {
            val filter =
                if (tabIndex == HomeState.TAB_INDEX_UPCOMING) {
                    LaunchFilter.UPCOMING
                } else {
                    LaunchFilter.PACK
                }
            selectFilter(filter)
        }

        private fun selectFilter(filter: LaunchFilter) {
            if (state.value.selectedFilter == filter) return

            state.value
                .copy(
                    selectedFilter = filter,
                    selectedTabIndex =
                        if (filter == LaunchFilter.UPCOMING) {
                            HomeState.TAB_INDEX_UPCOMING
                        } else {
                            HomeState.TAB_INDEX_PAST
                        },
                ).sendToState()
        }

        private fun handleWatchAction(launchId: String) {
        }

        private fun dismissError() {
            state.value.copy(errorMessage = null).sendToState()
        }

        private fun dismissRateLimitError() {
            state.value.copy(rateLimitError = null).sendToState()
        }

        private fun retry() {
            dismissError()
        }

        private fun checkRateLimitStatus() {
            val rateLimitInfo = rateLimitInterceptor.getRateLimitInfo()
            if (rateLimitInfo != null) {
                state.value
                    .copy(
                        rateLimitError =
                            RateLimitError(
                                retryAfterSeconds = rateLimitInfo.remainingSeconds,
                                message = "", // Will be set in UI layer
                            ),
                    ).sendToState()
            }
        }

        private fun startRateLimitCountdown() {
            viewModelScope.launch {
                while (isActive) {
                    delay(COUNTDOWN_UPDATE_INTERVAL_MS)
                    val rateLimitInfo = rateLimitInterceptor.getRateLimitInfo()

                    if (rateLimitInfo != null && rateLimitInfo.remainingSeconds > 0) {
                        state.value
                            .copy(
                                rateLimitError =
                                    RateLimitError(
                                        retryAfterSeconds = rateLimitInfo.remainingSeconds,
                                        message = "", // Will be set in UI layer
                                    ),
                            ).sendToState()
                    } else if (state.value.rateLimitError != null) {
                        dismissRateLimitError()
                    }
                }
            }
        }
    }
