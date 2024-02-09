package com.weatherapp.core.helpers

import com.weatherapp.core.utils.ExceptionDescriptions
import com.weatherapp.core.utils.ExceptionTitles

object SetError {
    fun setErrorCard(errorTitle: String): String {
        return when (errorTitle) {
            ExceptionTitles.GPS_DISABLED -> ExceptionDescriptions.GPS_DISABLED_DESCR
            ExceptionTitles.NO_INTERNET_CONNECTION -> ExceptionDescriptions.NO_INTERNET_CONNECTION_DESCR
            ExceptionTitles.NO_PERMISSION -> ExceptionDescriptions.NO_PERMISSION_DESCR
            else -> ExceptionDescriptions.UNKNOWN_ERROR_DESCR
        }
    }
}