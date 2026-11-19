package com.umar.expensetracker.app.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.annotation.ExperimentalVoyagerApi
import cafe.adriel.voyager.core.registry.ScreenRegistry
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition

@OptIn(ExperimentalVoyagerApi::class)
@Composable
fun RootNavigator() {
    Navigator(
        screen = ScreenRegistry.get(ScreenFactory.Dashboard)
    ) { navigator ->
        SlideTransition(
            navigator = navigator,
            disposeScreenAfterTransitionEnd = true
        )
    }
}