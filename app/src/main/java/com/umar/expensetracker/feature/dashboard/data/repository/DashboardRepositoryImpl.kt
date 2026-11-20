package com.umar.expensetracker.feature.dashboard.data.repository

import com.umar.expensetracker.feature.dashboard.domain.model.DashboardSummary
import com.umar.expensetracker.feature.dashboard.domain.repository.DashboardRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DashboardRepositoryImpl @Inject constructor() : DashboardRepository {
    override fun getDashboardSummary(): Flow<DashboardSummary> = flow {
        emit(
            DashboardSummary(
                totalBalance = 24_500_000.0,
                totalIncome = 12_500_000.0,
                totalExpense = 4_850_000.0,
                transactionCount = 38
            )
        )
    }
}