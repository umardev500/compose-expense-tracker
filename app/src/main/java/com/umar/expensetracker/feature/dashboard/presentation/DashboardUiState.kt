package com.umar.expensetracker.feature.dashboard.presentation

import com.umar.expensetracker.feature.dashboard.domain.model.BudgetOverview
import com.umar.expensetracker.feature.dashboard.domain.model.CategorySpending
import com.umar.expensetracker.feature.dashboard.domain.model.DailyTrend
import com.umar.expensetracker.feature.dashboard.domain.model.DashboardSummary
import com.umar.expensetracker.feature.dashboard.domain.model.DashboardTransaction

enum class DashboardPeriod(val title: String) {
    TODAY("Today"),
    THIS_WEEK("This Week"),
    THIS_MONTH("This Month"),
    THIS_YEAR("This Year")
}

sealed interface DashboardUiState {
    data object Loading : DashboardUiState

    data class Success(
        val userName: String = "Umar Schweinsteiger",
        val summary: DashboardSummary = DashboardSummary(),
        val selectedPeriod: DashboardPeriod = DashboardPeriod.THIS_MONTH,
        val isBalanceVisible: Boolean = true,
        val recentTransactions: List<DashboardTransaction> = emptyList(),
        val categorySpendings: List<CategorySpending> = emptyList(),
        val weeklyTrends: List<DailyTrend> = emptyList(),
        val budgetOverview: BudgetOverview = BudgetOverview(totalBudget = 4000.0, spentAmount = 2875.40),
        val percentageChange: Double = 12.4
    ) : DashboardUiState

    data class Error(val message: String) : DashboardUiState
}
