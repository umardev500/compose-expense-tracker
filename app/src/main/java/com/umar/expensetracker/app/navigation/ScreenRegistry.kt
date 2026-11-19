package com.umar.expensetracker.app.navigation

import cafe.adriel.voyager.core.registry.screenModule
import com.umar.expensetracker.feature.dashboard.presentation.DashboardScreen
import com.umar.expensetracker.feature.main.MainScreen

val screenModule = screenModule {
    register<ScreenFactory.Main> {
        MainScreen()
    }

    register<ScreenFactory.Dashboard> {
        DashboardScreen()
    }
}