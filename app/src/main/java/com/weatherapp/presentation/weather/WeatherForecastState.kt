package com.weatherapp.presentation.weather

import com.weatherapp.domain.model.Forecast

sealed interface WeatherForecastState {
    data class Success(val forecast: Forecast?): WeatherForecastState
    data class Error(val errorMessage: String?): WeatherForecastState

    object Loading: WeatherForecastState
}