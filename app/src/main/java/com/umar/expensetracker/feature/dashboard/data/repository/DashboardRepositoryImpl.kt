package com.umar.expensetracker.feature.dashboard.data.repository

import com.umar.expensetracker.feature.dashboard.domain.model.DashboardSummary
import com.umar.expensetracker.feature.dashboard.domain.repository.DashboardRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DashboardRepositoryImpl @Inject constructor() : DashboardRepository {
    override fun getDashboardSummary(): Flow<DashboardSummary> {
        TODO("Not yet implemented")
    }
}