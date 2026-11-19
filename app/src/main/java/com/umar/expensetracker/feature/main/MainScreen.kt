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
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import com.umar.expensetracker.app.navigation.HomeTab

class MainScreen : Screen {
    @Composable
    override fun Content() {

        TabNavigator(HomeTab) {
            Scaffold(
                bottomBar = {
                    NavigationBar {
                        val tabNavigator = LocalTabNavigator.current

                        listOf<Tab>(
                            HomeTab
                        ).forEach { tab ->
                            NavigationBarItem(
                                selected = tabNavigator.current == tab,
                                onClick = {},
                                icon = {
                                    Icon(
                                        painter = tab.options.icon!!,
                                        contentDescription = tab.options.title
                                    )
                                },
                                label = {
                                    Text(tab.options.title)
                                }
                            )
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
