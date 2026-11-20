package com.umar.expensetracker.feature.setting.presentation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey

class SettingScreen : Screen {
    override val key: ScreenKey = "setting_screen"

    @Composable
    override fun Content() {
        SettingContent()
    }
}