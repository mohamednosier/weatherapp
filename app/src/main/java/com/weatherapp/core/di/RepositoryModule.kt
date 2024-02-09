package com.weatherapp.core.di

import com.weatherapp.data.repository.ForecastRepositoryImpl
import com.weatherapp.domain.repository.ForecastRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindForecastRepository(forecastRepositoryImpl: ForecastRepositoryImpl): ForecastRepository
}