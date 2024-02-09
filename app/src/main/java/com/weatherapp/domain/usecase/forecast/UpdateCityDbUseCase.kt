package com.weatherapp.domain.usecase.forecast

import com.weatherapp.data.repository.ForecastRepositoryImpl
import com.weatherapp.domain.model.City
import javax.inject.Inject

class UpdateCityDbUseCase @Inject constructor(private val forecastRepositoryImpl: ForecastRepositoryImpl) {
    suspend fun updateCityDb(city: City) = forecastRepositoryImpl.updateCity(city)
}