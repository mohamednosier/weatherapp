package com.weatherapp.domain.usecase.forecast

import com.weatherapp.data.repository.ForecastRepositoryImpl
import javax.inject.Inject

class GetCityFromDbUseCase @Inject constructor(private val forecastRepositoryImpl: ForecastRepositoryImpl) {
    fun getCityFromDb() = forecastRepositoryImpl.getCity()
}