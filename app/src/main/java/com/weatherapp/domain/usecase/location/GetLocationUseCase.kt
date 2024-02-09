package com.weatherapp.domain.usecase.location

import com.weatherapp.data.location.DefaultLocationTracker
import javax.inject.Inject

class GetLocationUseCase @Inject constructor(private val defaultLocationTracker: DefaultLocationTracker) {
    suspend fun getLocation() = defaultLocationTracker.getCurrentLocation()
}