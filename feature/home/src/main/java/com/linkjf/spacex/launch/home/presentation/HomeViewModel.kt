package com.linkjf.spacex.launch.home.presentation

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.linkjf.spacex.launch.designsystem.components.LaunchListItem
import com.linkjf.spacex.launch.home.domain.usecase.GetPastLaunchesUseCase
import com.linkjf.spacex.launch.home.domain.usecase.GetUpcomingLaunchesUseCase
import com.linkjf.spacex.launch.mvi.StateViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
    @Inject
    constructor(
        private val getUpcomingLaunches: GetUpcomingLaunchesUseCase,
        private val getPastLaunches: GetPastLaunchesUseCase,
    ) : StateViewModel<HomeState, HomeEvent, HomeAction>(HomeState()) {
        val upcomingLaunches: Flow<PagingData<LaunchListItem>> =
            getUpcomingLaunches().cachedIn(viewModelScope)
        val pastLaunches: Flow<PagingData<LaunchListItem>> =
            getPastLaunches().cachedIn(viewModelScope)

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
            val filter = if (tabIndex == 0) LaunchFilter.UPCOMING else LaunchFilter.PACK
            selectFilter(filter)
        }

        private fun selectFilter(filter: LaunchFilter) {
            if (state.value.selectedFilter == filter) return

            state.value
                .copy(
                    selectedFilter = filter,
                    selectedTabIndex = if (filter == LaunchFilter.UPCOMING) 0 else 1,
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
    }
