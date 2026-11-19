package com.umar.expensetracker.feature.dashboard.presentation

sealed interface DashboardUiState {
    data object Loading : DashboardUiState
}