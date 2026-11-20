package com.umar.expensetracker.feature.dashboard.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.hilt.getScreenModel

class DashboardScreen : Screen {
    override val key: ScreenKey = "dashboard_screen"

    @Composable
    override fun Content() {
        val screenModel = getScreenModel<DashboardScreenModel>()
        val uiState by screenModel.uiState.collectAsState()

        DashboardContent(
            uiState
        )
    }
}