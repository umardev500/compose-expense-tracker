package com.umar.expensetracker.app.navigation

import cafe.adriel.voyager.core.registry.ScreenProvider

sealed class ScreenFactory : ScreenProvider {
    data object Dashboard : ScreenFactory()
}