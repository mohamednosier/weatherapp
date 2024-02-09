package com.weatherapp.domain.usecase.forecast

import com.weatherapp.data.repository.ForecastRepositoryImpl
import com.weatherapp.domain.model.City
import javax.inject.Inject

class AddCityToDbUseCase @Inject constructor(private val forecastRepositoryImpl: ForecastRepositoryImpl) {
    suspend fun addCityDb(city: City) = forecastRepositoryImpl.addCity(city)
}