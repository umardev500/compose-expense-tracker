package com.umar.expensetracker.feature.report.presentation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey

class ReportScreen : Screen {
    override val key: ScreenKey = "report_screen"

    @Composable
    override fun Content() {
        ReportContent()
    }
}