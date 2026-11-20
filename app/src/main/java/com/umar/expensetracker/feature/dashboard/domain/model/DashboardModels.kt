package com.umar.expensetracker.feature.dashboard.domain.model

enum class TransactionType {
    INCOME,
    EXPENSE
}

enum class ExpenseCategory(val displayName: String) {
    FOOD_AND_DINING("Food & Dining"),
    SHOPPING("Shopping"),
    HOUSING("Housing & Bills"),
    TRANSPORTATION("Transportation"),
    ENTERTAINMENT("Entertainment"),
    HEALTH("Health & Medical"),
    SALARY("Salary & Income"),
    INVESTMENT("Investment"),
    OTHER("Other")
}

data class DashboardTransaction(
    val id: String,
    val title: String,
    val subtitle: String,
    val amount: Double,
    val type: TransactionType,
    val category: ExpenseCategory,
    val formattedDate: String
)

data class CategorySpending(
    val category: ExpenseCategory,
    val name: String,
    val amount: Double,
    val percentage: Float
)

data class DailyTrend(
    val dayLabel: String,
    val amount: Double,
    val isToday: Boolean = false,
    val isPeak: Boolean = false
)

data class BudgetOverview(
    val totalBudget: Double,
    val spentAmount: Double
) {
    val remainingAmount: Double
        get() = (totalBudget - spentAmount).coerceAtLeast(0.0)

    val progress: Float
        get() = if (totalBudget > 0) (spentAmount / totalBudget).toFloat().coerceIn(0f, 1f) else 0f

    val isOverBudget: Boolean
        get() = spentAmount > totalBudget
}
