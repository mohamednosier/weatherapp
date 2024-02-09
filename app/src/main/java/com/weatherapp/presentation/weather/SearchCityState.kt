package com.weatherapp.presentation.weather

import com.weatherapp.domain.model.Forecast

sealed interface SearchCityState {
    data class Success(val forecast: Forecast?): SearchCityState
    data class Error(val errorMessage: String?): SearchCityState

    object Loading: SearchCityState
}