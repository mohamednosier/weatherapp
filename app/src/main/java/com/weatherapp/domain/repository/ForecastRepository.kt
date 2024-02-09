package com.weatherapp.domain.repository

import com.weatherapp.domain.model.City
import com.weatherapp.domain.model.Forecast
import com.weatherapp.core.common.Resource

interface ForecastRepository {
    suspend fun getForecastData(latitude: Double, longitude: Double, ): Resource<Forecast>

    suspend fun getForecastDataWithCityName(cityName: String): Resource<Forecast>

    suspend fun addForecastWeather(forecast: Forecast)

    suspend fun addCity(city: City)

    fun getForecastWeather() : Forecast?

    fun getCity() : City

    suspend fun updateForecastWeather(forecast: Forecast)

    suspend fun updateCity(city: City)
}