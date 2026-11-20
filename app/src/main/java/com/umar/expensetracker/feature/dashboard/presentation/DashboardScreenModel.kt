package com.umar.expensetracker.feature.dashboard.presentation

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.umar.expensetracker.feature.dashboard.domain.model.BudgetOverview
import com.umar.expensetracker.feature.dashboard.domain.model.CategorySpending
import com.umar.expensetracker.feature.dashboard.domain.model.DailyTrend
import com.umar.expensetracker.feature.dashboard.domain.model.DashboardTransaction
import com.umar.expensetracker.feature.dashboard.domain.model.ExpenseCategory
import com.umar.expensetracker.feature.dashboard.domain.model.TransactionType
import com.umar.expensetracker.feature.dashboard.domain.usecase.GetDashboardSummaryUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
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

    private fun loadDashboard() {
        screenModelScope.launch {
            _uiState.value = DashboardUiState.Loading
            getDashboardSummary()
                .catch { error ->
                    _uiState.value = DashboardUiState.Error(
                        error.message ?: "Failed to load dashboard summary"
                    )
                }
                .collect { summary ->
                    _uiState.value = DashboardUiState.Success(
                        summary = summary,
                        recentTransactions = createDefaultTransactions(),
                        categorySpendings = createDefaultCategories(),
                        weeklyTrends = createDefaultWeeklyTrends(),
                        budgetOverview = BudgetOverview(
                            totalBudget = 4000.0,
                            spentAmount = summary.totalExpense
                        )
                    )
                }
        }
    }

    fun toggleBalanceVisibility() {
        _uiState.update { current ->
            if (current is DashboardUiState.Success) {
                current.copy(isBalanceVisible = !current.isBalanceVisible)
            } else current
        }
    }

    fun refresh() {
        loadDashboard()
    }

    private fun createDefaultTransactions(): List<DashboardTransaction> = listOf(
        DashboardTransaction(
            id = "1",
            title = "Whole Foods Market",
            subtitle = "Groceries • Supermarket",
            amount = 84.50,
            type = TransactionType.EXPENSE,
            category = ExpenseCategory.FOOD_AND_DINING,
            formattedDate = "Today, 02:45 PM"
        ),
        DashboardTransaction(
            id = "2",
            title = "TechCorp Inc. Salary",
            subtitle = "Monthly payroll direct deposit",
            amount = 3210.00,
            type = TransactionType.INCOME,
            category = ExpenseCategory.SALARY,
            formattedDate = "Yesterday, 09:00 AM"
        ),
        DashboardTransaction(
            id = "3",
            title = "Netflix Subscription",
            subtitle = "Premium 4K streaming plan",
            amount = 19.99,
            type = TransactionType.EXPENSE,
            category = ExpenseCategory.ENTERTAINMENT,
            formattedDate = "18 Nov, 08:15 PM"
        ),
        DashboardTransaction(
            id = "4",
            title = "Uber Ride",
            subtitle = "Downtown commute",
            amount = 24.30,
            type = TransactionType.EXPENSE,
            category = ExpenseCategory.TRANSPORTATION,
            formattedDate = "17 Nov, 07:30 PM"
        ),
        DashboardTransaction(
            id = "5",
            title = "Apple Store",
            subtitle = "AirPods Pro USB-C",
            amount = 249.00,
            type = TransactionType.EXPENSE,
            category = ExpenseCategory.SHOPPING,
            formattedDate = "16 Nov, 04:12 PM"
        ),
        DashboardTransaction(
            id = "6",
            title = "Freelance UI Design",
            subtitle = "Design system delivery",
            amount = 850.00,
            type = TransactionType.INCOME,
            category = ExpenseCategory.INVESTMENT,
            formattedDate = "15 Nov, 03:20 PM"
        ),
        DashboardTransaction(
            id = "7",
            title = "City Power & Water",
            subtitle = "Monthly utility billing",
            amount = 135.50,
            type = TransactionType.EXPENSE,
            category = ExpenseCategory.HOUSING,
            formattedDate = "14 Nov, 10:00 AM"
        )
    )

    private fun createDefaultCategories(): List<CategorySpending> = listOf(
        CategorySpending(
            category = ExpenseCategory.FOOD_AND_DINING,
            name = "Food & Dining",
            amount = 940.00,
            percentage = 0.327f
        ),
        CategorySpending(
            category = ExpenseCategory.SHOPPING,
            name = "Shopping",
            amount = 680.00,
            percentage = 0.236f
        ),
        CategorySpending(
            category = ExpenseCategory.HOUSING,
            name = "Housing & Bills",
            amount = 550.00,
            percentage = 0.191f
        ),
        CategorySpending(
            category = ExpenseCategory.TRANSPORTATION,
            name = "Transportation",
            amount = 320.00,
            percentage = 0.111f
        ),
        CategorySpending(
            category = ExpenseCategory.ENTERTAINMENT,
            name = "Entertainment",
            amount = 240.00,
            percentage = 0.083f
        ),
        CategorySpending(
            category = ExpenseCategory.HEALTH,
            name = "Health & Medical",
            amount = 145.40,
            percentage = 0.052f
        )
    )

    private fun createDefaultWeeklyTrends(): List<DailyTrend> = listOf(
        DailyTrend(dayLabel = "Mon", amount = 280.0),
        DailyTrend(dayLabel = "Tue", amount = 490.0),
        DailyTrend(dayLabel = "Wed", amount = 150.0),
        DailyTrend(dayLabel = "Thu", amount = 720.0, isPeak = true),
        DailyTrend(dayLabel = "Fri", amount = 340.0),
        DailyTrend(dayLabel = "Sat", amount = 560.0),
        DailyTrend(dayLabel = "Sun", amount = 335.4, isToday = true)
    )
}
