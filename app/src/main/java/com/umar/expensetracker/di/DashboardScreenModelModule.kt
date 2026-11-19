package com.umar.expensetracker.di

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.hilt.ScreenModelKey
import com.umar.expensetracker.feature.dashboard.presentation.DashboardScreenModel
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.multibindings.IntoMap

@Module
@InstallIn(ActivityComponent::class)
abstract class DashboardScreenModelModule {

    @Binds
    @IntoMap
    @ScreenModelKey(DashboardScreenModel::class)
    abstract fun bindDashboardScreenModel(
        model: DashboardScreenModel
    ): ScreenModel
}