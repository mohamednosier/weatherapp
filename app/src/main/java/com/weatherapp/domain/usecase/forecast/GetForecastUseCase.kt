package com.weatherapp.domain.usecase.forecast

import com.weatherapp.data.repository.ForecastRepositoryImpl
import com.weatherapp.domain.model.Forecast
import com.weatherapp.core.common.Resource
import javax.inject.Inject

class GetForecastUseCase @Inject constructor(private val forecastRepositoryImpl: ForecastRepositoryImpl) {
    suspend fun getForecast(latitude: Double, longitude: Double): Resource<Forecast> =
        forecastRepositoryImpl.getForecastData(latitude, longitude)
}