package com.umar.expensetracker.feature.report.presentation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen

class ReportScreen : Screen {
    @Composable
    override fun Content() {
        ReportContent()
    }
}