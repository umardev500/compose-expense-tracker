
package com.umar.expensetracker.app.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CandlestickChart
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.CandlestickChart
import androidx.compose.material.icons.outlined.Eco
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.rounded.Home
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.core.registry.ScreenRegistry
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions

interface AppTab : Tab {
    val icons: TabIcons
}

data class TabIcons(
    val selected: ImageVector,
    val unselected: ImageVector
)

object HomeTab : AppTab {
    private fun readResolve(): Any = HomeTab

    override val icons = TabIcons(
        selected = Icons.Filled.Home,
        unselected = Icons.Rounded.Home
    )

    override val options: TabOptions
        @Composable get() {
            val icon = rememberVectorPainter(icons.unselected)

            return TabOptions(
                index = 0u,
                title = "Home",
                icon = icon
            )
        }

    @Composable
    override fun Content() {
        Navigator(ScreenRegistry.get(ScreenFactory.Dashboard))
    }
}

object ReportTab : AppTab {
    private fun readResolve(): Any = ReportTab

    override val icons = TabIcons(
        selected = Icons.Filled.CandlestickChart,
        unselected = Icons.Outlined.CandlestickChart
    )

    override val options: TabOptions
        @Composable get() {
            val icon = rememberVectorPainter(icons.unselected)

            return TabOptions(
                index = 1u,
                title = "Report",
                icon = icon
            )
        }

    @Composable
    override fun Content() {
        Navigator(ScreenRegistry.get(ScreenFactory.Setting))
    }
}

object PlanTab : AppTab {
    private fun readResolve(): Any = PlanTab

    override val icons = TabIcons(
        selected = Icons.Filled.Eco,
        unselected = Icons.Outlined.Eco
    )

    override val options: TabOptions
        @Composable get() {
            val icon = rememberVectorPainter(icons.unselected)

            return TabOptions(
                index = 2u,
                title = "Plan",
                icon = icon
            )
        }

    @Composable
    override fun Content() {
        Navigator(ScreenRegistry.get(ScreenFactory.Setting))
    }
}

object SettingTab : AppTab {
    private fun readResolve(): Any = SettingTab

    override val icons = TabIcons(
        selected = Icons.Filled.Settings,
        unselected = Icons.Outlined.Settings
    )

    override val options: TabOptions
        @Composable get() {
            val icon = rememberVectorPainter(icons.unselected)

            return TabOptions(
                index = 3u,
                title = "Setting",
                icon = icon
            )
        }

    @Composable
    override fun Content() {
        Navigator(ScreenRegistry.get(ScreenFactory.Setting))
    }
}

val appTabs = listOf<AppTab>(
    HomeTab,
    ReportTab,
    PlanTab,
    SettingTab
)
