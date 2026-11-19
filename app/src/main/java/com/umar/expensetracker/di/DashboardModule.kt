    package com.umar.expensetracker.di

    import com.umar.expensetracker.feature.dashboard.data.repository.DashboardRepositoryImpl
    import com.umar.expensetracker.feature.dashboard.domain.repository.DashboardRepository
    import dagger.Binds
    import dagger.Module
    import dagger.hilt.InstallIn
    import dagger.hilt.components.SingletonComponent
    import javax.inject.Singleton

    @Module
    @InstallIn(SingletonComponent::class)
    abstract class DashboardModule {

        @Binds
        @Singleton
        abstract fun bindDashboardRepository(
            implementation: DashboardRepositoryImpl
        ): DashboardRepository
    }