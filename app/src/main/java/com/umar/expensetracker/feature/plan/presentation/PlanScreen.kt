package com.umar.expensetracker.feature.plan.presentation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey

class PlanScreen : Screen {
    override val key: ScreenKey = "plan_screen"

    @Composable
    override fun Content() {
        PlanContent()
    }
}