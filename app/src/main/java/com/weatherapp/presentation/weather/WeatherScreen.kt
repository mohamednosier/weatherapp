package com.weatherapp.presentation.weather

import android.annotation.SuppressLint
import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.weatherapp.R
import com.weatherapp.core.utils.AppStrings
import com.weatherapp.domain.model.Forecast
import com.weatherapp.core.helpers.EpochConverter
import com.weatherapp.core.helpers.SetError
import com.weatherapp.core.utils.Constants
import com.weatherapp.presentation.component.*
import com.weatherapp.core.utils.ErrorCardConsts
import com.weatherapp.core.utils.ExceptionTitles


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun WeatherScreen(viewModel: WeatherViewModel) {
    val currentWeatherState by viewModel.weatherForecastState.collectAsState()
    val activity = (LocalContext.current as? Activity)

    Scaffold(modifier = Modifier.fillMaxSize()) {
        BackgroundImage()
        MenuIcon ( viewModel )
        WeatherSection(currentWeatherState) { activity?.finish() }
    }
}

@Composable
private fun BackgroundImage() {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(id = R.drawable.background),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
private fun WeatherSection(currentWeatherState: WeatherForecastState, errorCardOnClick: () -> Unit) {
    when (currentWeatherState) {
        is WeatherForecastState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressBar(modifier = Modifier.size(LocalConfiguration.current.screenWidthDp.dp / 3))
            }
        }
        is WeatherForecastState.Success -> {
            if (currentWeatherState.forecast != null) {
                CurrentWeatherSection(currentWeatherState.forecast)
                DetailsSection(currentWeatherState.forecast)
            }
        }
        is WeatherForecastState.Error -> {
            ErrorCard(
                modifier = Modifier.fillMaxSize(),
                errorTitle = currentWeatherState.errorMessage ?: ExceptionTitles.UNKNOWN_ERROR,
                errorDescription = SetError.setErrorCard(
                    currentWeatherState.errorMessage ?: ExceptionTitles.UNKNOWN_ERROR
                ),
                errorButtonText = ErrorCardConsts.BUTTON_TEXT,
                errorCardOnClick,
                cardModifier = Modifier
                    .fillMaxWidth()
                    .height(LocalConfiguration.current.screenHeightDp.dp / 4 + 48.dp)
                    .padding(horizontal = 64.dp)
            )
        }
    }
}

@Composable
private fun CurrentWeatherSection(todayWeather: Forecast) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(top = 72.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = todayWeather.cityDtoData.cityName,
            style = MaterialTheme.typography.h2
        )
        Text(
            text = "${todayWeather.weatherList[0].weatherData.temp.toInt()}${AppStrings.degree}",
            style = MaterialTheme.typography.h1
        )
        Text(
            text = todayWeather.weatherList[0].weatherStatus[0].description,
            style = MaterialTheme.typography.h3,
            color = Color.Gray
        )
        Text(
            text = "H:${todayWeather.cityDtoData.coordinate.longitude}°  L:${todayWeather.cityDtoData.coordinate.latitude}°",
            style = MaterialTheme.typography.h3
        )
    }
}

@Composable
private fun DetailsSection(forecast: Forecast) {
    Box(
        modifier = Modifier.fillMaxSize(),
        Alignment.BottomCenter
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(LocalConfiguration.current.screenHeightDp.dp / 2),
            backgroundColor = MaterialTheme.colors.onSurface,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                ForecastSection(forecast)
                WeatherDetailSection(forecast)
            }
        }
    }
}

@Composable
private fun ForecastSection(forecastData: Forecast) {
    ForecastTitle(text = AppStrings.hourly_forecast)
    ForecastLazyRow(forecasts = forecastData.weatherList.take(8))
    ForecastTitle(text = AppStrings.daily_forecast)
    ForecastLazyRow(forecasts = forecastData.weatherList.takeLast(32))
}

@Composable
private fun WeatherDetailSection(currentWeather: Forecast) {
    CurrentWeatherDetailRow(
        title1 = AppStrings.temp,
        value1 = "${currentWeather.weatherList[0].weatherData.temp}${AppStrings.degree}",
        title2 = AppStrings.feels_like,
        value2 = "${currentWeather.weatherList[0].weatherData.feelsLike}${AppStrings.degree}"
    )
    CurrentWeatherDetailRow(
        title1 = AppStrings.cloudiness,
        value1 = "${currentWeather.weatherList[0].cloudiness.cloudiness}%",
        title2 = AppStrings.humidity,
        value2 = "${currentWeather.weatherList[0].weatherData.humidity}%"
    )
    CurrentWeatherDetailRow(
        title1 = AppStrings.sunrise,
        value1 = "${EpochConverter.readTimestamp(currentWeather.cityDtoData.sunrise)}AM",
        title2 = AppStrings.sunset,
        value2 = "${EpochConverter.readTimestamp(currentWeather.cityDtoData.sunset)}PM"
    )
    CurrentWeatherDetailRow(
        title1 = AppStrings.wind,
        value1 = "${currentWeather.weatherList[0].wind.speed}${AppStrings.metric}",
        title2 = AppStrings.pressure,
        value2 = "${currentWeather.weatherList[0].weatherData.pressure}"
    )
}

@Composable
private fun MenuIcon(viewModel:WeatherViewModel) {
    val searchCityState by viewModel.searchCityState.collectAsState()
    Column(
        modifier = Modifier

            .padding(start = 16.dp, end = 16.dp, top = 16.dp)
    ) {
        SearchCityContent(
            viewModel = viewModel,
            searchCityState = searchCityState,
        )
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
private fun SearchCityContent(
    viewModel: WeatherViewModel,
    searchCityState: SearchCityState,

    ) {
    SearchField(viewModel)
    if (viewModel.isCitySearched) {
        when (searchCityState) {
            is SearchCityState.Loading -> {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CircularProgressBar(
                        modifier = Modifier
                            .size(LocalConfiguration.current.screenWidthDp.dp / 3)
                            .padding(top = 16.dp),
                    )
                }
            }
            is SearchCityState.Success -> {
                if (searchCityState.forecast != null) {
                    viewModel.catchCatchedSearched(searchCityState.forecast.cityDtoData.coordinate.latitude,searchCityState.forecast.cityDtoData.coordinate.longitude)
                    }
            }
            is SearchCityState.Error -> {
                SearchResultErrorMessage(searchCityState.errorMessage, viewModel)
            }
        }
    } else {

    }
}

@Composable
private fun SearchField(viewModel: WeatherViewModel) {
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = viewModel.searchFieldValue,
        onValueChange = {
            viewModel.updateSearchField(it)
                        },
        label = {
            Text(text = AppStrings.placeholder)
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        maxLines = 1,
        trailingIcon = {
            IconButton(onClick = { viewModel.searchCityClick() }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_search_24),
                    contentDescription = null
                )
            }
        }
    )
}
@Composable
private fun SearchResultErrorMessage(errorMessage: String?, viewModel: WeatherViewModel) {
    ErrorCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        errorTitle = AppStrings.error_title,
        errorDescription = errorMessage ?: Constants.UNKNOWN_ERROR,
        errorButtonText = ErrorCardConsts.BUTTON_TEXT,
        onClick = { viewModel.errorOnClick() },
        cardModifier = Modifier
            .fillMaxWidth()
            .height(LocalConfiguration.current.screenHeightDp.dp / 4 + 48.dp)
    )
}
