package com.weatherapp.data.repository

import com.weatherapp.data.datasource.local.db.CityLocalDataSource
import com.weatherapp.data.datasource.local.db.ForecastLocalDataSource
import com.weatherapp.data.datasource.remote.api.ForecastRemoteDataSource
import com.weatherapp.data.mapper.CityEntityMapper
import com.weatherapp.data.mapper.ForecastDtoMapper
import com.weatherapp.data.mapper.ForecastEntityMapper
import com.weatherapp.domain.model.City
import com.weatherapp.domain.model.Forecast
import com.weatherapp.domain.repository.ForecastRepository
import com.weatherapp.core.common.Resource
import com.weatherapp.core.utils.Constants
import javax.inject.Inject

class ForecastRepositoryImpl @Inject constructor(
    private val forecastRemoteDataSource: ForecastRemoteDataSource,
    private val forecastLocalDataSource: ForecastLocalDataSource,
    private val cityLocalDataSource: CityLocalDataSource,
    private val dtoMapper: ForecastDtoMapper,
    private val entityMapper: ForecastEntityMapper,
    private val cityEntityMapper: CityEntityMapper
) : ForecastRepository {
    override suspend fun getForecastData(
        latitude: Double,
        longitude: Double
    ): Resource<Forecast> {
        return try {
            Resource.Success(
                dtoMapper.mapFromEntity(
                    forecastRemoteDataSource.getForecastData(
                        latitude,
                        longitude
                    )
                )
            )
        } catch (e: Exception) {
            Resource.Error(e.message ?: Constants.UNKNOWN_ERROR)
        }
    }

    override suspend fun getForecastDataWithCityName(cityName: String): Resource<Forecast> {
        return try {
            Resource.Success(
                dtoMapper.mapFromEntity(
                    forecastRemoteDataSource.getForecastDataWithCityName(cityName)
                )
            )
        } catch (e: Exception) {
            Resource.Error(e.message ?: Constants.UNKNOWN_ERROR)
        }
    }

    override suspend fun addForecastWeather(forecast: Forecast) {
        forecastLocalDataSource.addForecastWeather(
            entityMapper.entityFromModel(forecast)
        )
    }

    override suspend fun addCity(city: City) {
        forecastLocalDataSource.addCity(
            cityEntityMapper.entityFromModel(city)
        )
    }

    override fun getForecastWeather(): Forecast? {
        return if (forecastLocalDataSource.getForecastWeather().isNullOrEmpty()) {
            null
        } else {
            entityMapper.mapFromEntity(
                forecastLocalDataSource.getForecastWeather(),
                cityLocalDataSource.getCity()
            )
        }
    }

    override fun getCity(): City {
        return cityEntityMapper.mapFromEntity(forecastLocalDataSource.getCity())
    }

    override suspend fun updateForecastWeather(forecast: Forecast) {
        forecastLocalDataSource.updateForecastWeather(
            entityMapper.entityFromModel(forecast)
        )
    }

    override suspend fun updateCity(city: City) {
        forecastLocalDataSource.updateCity(
            cityEntityMapper.entityFromModel(city)
        )
    }
}