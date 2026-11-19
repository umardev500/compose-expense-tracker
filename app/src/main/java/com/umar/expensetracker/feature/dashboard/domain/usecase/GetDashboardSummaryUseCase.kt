package com.umar.expensetracker.feature.dashboard.domain.usecase

import com.umar.expensetracker.feature.dashboard.domain.model.DashboardSummary
import com.umar.expensetracker.feature.dashboard.domain.repository.DashboardRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDashboardSummaryUseCase @Inject constructor(
    private val repository: DashboardRepository
) {
    operator fun invoke(): Flow<DashboardSummary> {
        return repository.getDashboardSummary()
    }
}