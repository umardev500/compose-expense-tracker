package com.umar.expensetracker.feature.dashboard.domain.model

data class DashboardSummary(
    val totalBalance: Double = 0.0,
    val totalIncome: Double = 0.0,
    val totalExpense: Double = 0.0,
    val transactionCount: Int = 0
)