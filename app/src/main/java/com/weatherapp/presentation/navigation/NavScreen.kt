package com.weatherapp.presentation.navigation

sealed class NavScreen(val route: String) {
    object WeatherScreen : NavScreen(NavRoutes.weatherScreen)
}
