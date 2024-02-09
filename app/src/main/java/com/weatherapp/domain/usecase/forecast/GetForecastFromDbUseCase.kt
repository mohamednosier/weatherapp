package com.weatherapp.domain.usecase.forecast

import com.weatherapp.data.repository.ForecastRepositoryImpl
import com.weatherapp.domain.model.Forecast
import javax.inject.Inject

class GetForecastFromDbUseCase @Inject constructor(private val forecastRepositoryImpl: ForecastRepositoryImpl) {
    fun getForecastFromDbUseCase() : Forecast? = forecastRepositoryImpl.getForecastWeather()
}