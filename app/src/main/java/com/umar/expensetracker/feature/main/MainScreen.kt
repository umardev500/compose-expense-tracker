package com.umar.expensetracker.feature.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.TabNavigator
import com.umar.expensetracker.app.navigation.HomeTab
import com.umar.expensetracker.app.navigation.appTabs

class MainScreen : Screen {
    override val key: ScreenKey = "main_screen"

    @Composable
    override fun Content() {

        TabNavigator(HomeTab) {
            Scaffold(
                bottomBar = {
                    NavigationBar {
                        val tabNavigator = LocalTabNavigator.current

                        appTabs.forEach { tab ->
                            val selected = tabNavigator.current == tab
                            NavigationBarItem(
                                selected = selected,
                                onClick = {
                                    tabNavigator.current = tab
                                },
                                icon = {
                                    Icon(
                                        imageVector = if (selected) {
                                            tab.icons.selected
                                        } else {
                                            tab.icons.unselected
                                        },
                                        contentDescription = tab.options.title
                                    )
                                },
                                label = {
                                    Text(tab.options.title)
                                })
                        }
                    }
                }) { paddingValues ->
                Box(
                    modifier = Modifier.padding(paddingValues)
                ) {
                    CurrentTab()
                }
            }
        }
    }
}
