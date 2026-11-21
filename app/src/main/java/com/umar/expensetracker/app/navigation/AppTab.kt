package com.umar.expensetracker.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.core.registry.ScreenRegistry
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.umar.expensetracker.ui.icon.HeroIcons

interface AppTab : Tab {
    val index: UShort
    val title: String
    val destination: ScreenFactory
    val icons: TabIcons

    @Composable
    override fun Content() {
        Navigator(ScreenRegistry.get(destination))
    }
}

data class TabIcons(
    val selected: ImageVector,
    val unselected: ImageVector
)

@Composable
private fun AppTab.rememberTabOptions(): TabOptions {
    val icon = rememberVectorPainter(icons.unselected)
    return TabOptions(
        index = index,
        title = title,
        icon = icon
    )
}

object HomeTab : AppTab {
    private fun readResolve(): Any = HomeTab

    override val index: UShort = 0u
    override val title: String = "Home"
    override val destination: ScreenFactory = ScreenFactory.Dashboard

    override val icons = TabIcons(
        selected = HeroIcons.HomeSolid,
        unselected = HeroIcons.Home
    )

    override val options: TabOptions
        @Composable get() = rememberTabOptions()
}

object ReportTab : AppTab {
    private fun readResolve(): Any = ReportTab

    override val index: UShort = 1u
    override val title: String = "Report"
    override val destination: ScreenFactory = ScreenFactory.Report

    override val icons = TabIcons(
        selected = HeroIcons.ArrowTrendingUp,
        unselected = HeroIcons.ArrowTrendingUp
    )

    override val options: TabOptions
        @Composable get() = rememberTabOptions()
}

object PlanTab : AppTab {
    private fun readResolve(): Any = PlanTab

    override val index: UShort = 2u
    override val title: String = "Plan"
    override val destination: ScreenFactory = ScreenFactory.Plan

    override val icons = TabIcons(
        selected = HeroIcons.SparklesSolid,
        unselected = HeroIcons.Sparkles
    )

    override val options: TabOptions
        @Composable get() = rememberTabOptions()
}

object SettingTab : AppTab {
    private fun readResolve(): Any = SettingTab

    override val index: UShort = 3u
    override val title: String = "Setting"
    override val destination: ScreenFactory = ScreenFactory.Setting

    override val icons = TabIcons(
        selected = HeroIcons.Cog6ToothSolid,
        unselected = HeroIcons.Cog6Tooth
    )

    override val options: TabOptions
        @Composable get() = rememberTabOptions()
}

val appTabs: List<AppTab> = listOf(
    HomeTab,
    ReportTab,
    PlanTab,
    SettingTab
)