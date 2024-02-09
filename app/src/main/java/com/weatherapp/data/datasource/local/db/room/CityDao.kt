package com.weatherapp.data.datasource.local.db.room

import androidx.room.*
import com.weatherapp.data.datasource.local.db.entity.CityEntity

@Dao
interface CityDao {
    @Insert
    suspend fun addCity(cityEntity: CityEntity)

    @Query("SELECT * FROM city_data")
    fun getCity(): CityEntity

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateCity(cityEntity: CityEntity)
}