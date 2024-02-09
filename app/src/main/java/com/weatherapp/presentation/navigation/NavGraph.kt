package com.weatherapp.presentation.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.weatherapp.presentation.weather.WeatherScreen
import com.weatherapp.presentation.weather.WeatherViewModel

@Composable
fun NavGraph(
    startDestination: String = NavScreen.WeatherScreen.route,
    weatherViewModel: WeatherViewModel
) {
    val navController = rememberNavController()

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) {
        NavHost(
            modifier = Modifier.padding(it),
            navController = navController,
            startDestination = startDestination
        ) {
            composable(NavScreen.WeatherScreen.route) {
                WeatherScreen(weatherViewModel)
            }
        }
    }
}