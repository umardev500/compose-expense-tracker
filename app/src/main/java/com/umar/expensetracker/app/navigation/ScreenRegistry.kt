package com.umar.expensetracker.app.navigation

import cafe.adriel.voyager.core.registry.screenModule
import com.umar.expensetracker.feature.dashboard.presentation.DashboardScreen

val screenModule = screenModule {
    register<ScreenFactory.Dashboard> {
        DashboardScreen()
    }
}