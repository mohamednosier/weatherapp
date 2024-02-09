package com.weatherapp.domain.usecase.forecast

import com.weatherapp.data.repository.ForecastRepositoryImpl
import javax.inject.Inject

class GetForecastWithCityNameUseCase @Inject constructor(private val forecastRepositoryImpl: ForecastRepositoryImpl) {
    suspend fun getForecast(cityName: String) =
        forecastRepositoryImpl.getForecastDataWithCityName(cityName)
}