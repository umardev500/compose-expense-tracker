package com.umar.expensetracker.feature.dashboard.domain.repository

import com.umar.expensetracker.feature.dashboard.domain.model.DashboardSummary
import kotlinx.coroutines.flow.Flow

interface DashboardRepository {
    fun getDashboardSummary(): Flow<DashboardSummary>
}