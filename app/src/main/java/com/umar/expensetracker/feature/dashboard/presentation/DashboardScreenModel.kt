package com.umar.expensetracker.feature.dashboard.presentation

import cafe.adriel.voyager.core.model.ScreenModel
import com.umar.expensetracker.feature.dashboard.domain.usecase.GetDashboardSummaryUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class DashboardScreenModel @Inject constructor(
    private val getDashboardSummary: GetDashboardSummaryUseCase
) : ScreenModel {

    private val _uiState =
        MutableStateFlow<DashboardUiState>(
            DashboardUiState.Loading
        )

    val uiState: StateFlow<DashboardUiState> =
        _uiState.asStateFlow()

    init {
        loadDashboard()
    }

    private fun loadDashboard() {}

    fun refresh() {
        loadDashboard()
    }
}