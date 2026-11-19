package com.umar.expensetracker.app.navigation

import cafe.adriel.voyager.core.registry.ScreenProvider

sealed class ScreenFactory : ScreenProvider {
    data object Main : ScreenFactory()
    data object Dashboard : ScreenFactory()
    data object Setting : ScreenFactory()
    data object Plan : ScreenFactory()
    data object Report : ScreenFactory()
}