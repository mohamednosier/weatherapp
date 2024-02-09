package com.weatherapp.data.datasource.local.db

import com.weatherapp.data.datasource.local.db.entity.CityEntity
import com.weatherapp.data.datasource.local.db.room.CityDao
import javax.inject.Inject

class CityLocalDataSource @Inject constructor(private val cityDao: CityDao) {

    fun getCity() = cityDao.getCity()

}