package com.umar.expensetracker.feature.dashboard.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun DashboardContent(
    uiState: DashboardUiState
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text("Hallo")
    }
}