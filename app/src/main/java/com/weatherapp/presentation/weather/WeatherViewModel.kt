package com.weatherapp.presentation.weather

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.weatherapp.domain.model.City
import com.weatherapp.domain.model.Forecast
import com.weatherapp.domain.usecase.forecast.*
import com.weatherapp.domain.usecase.location.GetLocationUseCase
import com.weatherapp.core.common.Resource
import com.weatherapp.core.utils.Constants
import com.weatherapp.core.utils.ExceptionTitles
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val addForecastDb: AddForecastToDbUseCase,
    private val addCityDb: AddCityToDbUseCase,
    private val updateCityDbUseCase: UpdateCityDbUseCase,
    private val getForecastDb: GetForecastFromDbUseCase,
    private val updateForecastDb: UpdateForecastDbUseCase,
    private val getForecast: GetForecastUseCase,
    private val getCurrentLocation: GetLocationUseCase,
    private val getForecastWithCityName: GetForecastWithCityNameUseCase,
) : ViewModel() {

    private val _weatherForecastState = MutableStateFlow<WeatherForecastState>(WeatherForecastState.Loading)
    val weatherForecastState = _weatherForecastState.asStateFlow()

    private val _searchCityState = MutableStateFlow<SearchCityState>(SearchCityState.Loading)
    val searchCityState = _searchCityState.asStateFlow()

    var searchFieldValue by mutableStateOf("")
        private set

    var isCitySearched by mutableStateOf(false)
        private set



    fun loadLocation() {
        _weatherForecastState.value = WeatherForecastState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val locationData = getCurrentLocation.getLocation()
                if (locationData != null) {
                    fetchForecast(locationData.latitude, locationData.longitude)
                } else if (isForecastCached()) {
                    getCachedForecast()
                } else {
                    _weatherForecastState.value = WeatherForecastState.Error(ExceptionTitles.NO_INTERNET_CONNECTION)
                }
            } catch (e: Exception) {
                if (isForecastCached()) {
                    getCachedForecast()
                } else {
                    _weatherForecastState.value = WeatherForecastState.Error(e.message)
                }
            }
        }
    }
    fun catchCatchedSearched(latitude: Double, longitude: Double) {
        viewModelScope.launch(Dispatchers.IO) {
            fetchForecast(latitude, longitude)
        }
    }
    private suspend fun fetchForecast(latitude: Double, longitude: Double) {
        when (val result = getForecast.getForecast(latitude, longitude)) {
            is Resource.Success -> {
                _weatherForecastState.value = WeatherForecastState.Success(result.data)
                if (result.data != null) {
                    if (!isForecastCached()) {
                        searchFieldValue=result.data.cityDtoData.cityName
                        cacheForecast(result.data, result.data.cityDtoData)
                    } else {
                        searchFieldValue=result.data.cityDtoData.cityName
                        updateCachedForecast(result.data, result.data.cityDtoData)
                    }
                }
            }
            is Resource.Error -> {
                _weatherForecastState.value = WeatherForecastState.Error(result.message)
            }
        }
    }

    private suspend fun cacheForecast(forecast: Forecast, city: City) {
        addForecastDb.addForecastToDbUseCase(
            forecast,
            forecast.weatherList.size
        )
        addCityDb.addCityDb(city)
    }

    suspend fun updateCachedForecast(forecast: Forecast, city: City) {
        updateForecastDb.updateForecastDbUseCase(
            forecast,
            forecast.weatherList.size
        )
        updateCityDbUseCase.updateCityDb(city)
    }

    // Data cannot be null.
    // Because before this function is called, it is checked for null with the isForecastCached() function.
    private fun getCachedForecast() {
        searchFieldValue= getForecastDb.getForecastFromDbUseCase()?.cityDtoData?.cityName.toString()
        _weatherForecastState.value =
            WeatherForecastState.Success(getForecastDb.getForecastFromDbUseCase())

    }

    private fun isForecastCached(): Boolean {
        return getForecastDb.getForecastFromDbUseCase() != null
    }

    fun errorOnClick() {
        _searchCityState.value = SearchCityState.Success(null)
    }

    fun searchCityClick() {
        isCitySearched = true
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (checkSearchFieldValue()) {
                    fetchForecastWithCityName(searchFieldValue)
                } else {
                    _searchCityState.value = SearchCityState.Error(Constants.FILL_FIELD)
                }
            } catch (e: Exception) {
                _searchCityState.value = SearchCityState.Error(e.message)
            }
        }
    }
    private suspend fun fetchForecastWithCityName(cityName: String) {
        when (val result = getForecastWithCityName.getForecast(cityName)) {
            is Resource.Success -> {
                _searchCityState.value = SearchCityState.Success(result.data)
            }
            is Resource.Error -> {
                _searchCityState.value = SearchCityState.Error(result.message)
            }
        }
    }
    private fun checkSearchFieldValue(): Boolean {
        return searchFieldValue.isNotEmpty()
    }

    fun updateSearchField(input: String) {
        searchFieldValue = input
    }
}