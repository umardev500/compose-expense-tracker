package com.umar.expensetracker.app.navigation

import cafe.adriel.voyager.core.registry.screenModule
import com.umar.expensetracker.feature.dashboard.presentation.DashboardScreen
import com.umar.expensetracker.feature.main.MainScreen
import com.umar.expensetracker.feature.plan.presentation.PlanScreen
import com.umar.expensetracker.feature.report.presentation.ReportScreen
import com.umar.expensetracker.feature.setting.presentation.SettingScreen

val screenModule = screenModule {
    register<ScreenFactory.Main> {
        MainScreen()
    }

    register<ScreenFactory.Dashboard> {
        DashboardScreen()
    }

    register<ScreenFactory.Report> {
        ReportScreen()
    }

    register<ScreenFactory.Plan> {
        PlanScreen()
    }

    register<ScreenFactory.Setting> {
        SettingScreen()
    }
}