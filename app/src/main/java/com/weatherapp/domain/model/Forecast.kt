package com.weatherapp.domain.model

data class Forecast(
    val weatherList: List<ForecastWeather>,
    val cityDtoData: City
)
