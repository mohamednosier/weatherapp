package com.weatherapp.presentation.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.weatherapp.R
import com.weatherapp.core.utils.AppStrings
import com.weatherapp.core.utils.Constants
import com.weatherapp.core.utils.ErrorCardConsts

@Composable
fun ErrorCard(
    modifier: Modifier = Modifier,
    errorTitle: String,
    errorDescription: String,
    errorButtonText: String,
    onClick: () -> Unit,
    cardModifier: Modifier,
) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Card(modifier = cardModifier) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .background(MaterialTheme.colors.error),
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceAround
                    ) {
                        Image(
                            modifier = Modifier.size(64.dp),
                            painter = painterResource(id = R.drawable.error),
                            contentDescription = null,
                            contentScale = ContentScale.Crop
                        )
                        Text(text = errorTitle, style = MaterialTheme.typography.h3)
                    }
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceAround
                    ) {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center ,
                            text = errorDescription,
                            style = MaterialTheme.typography.body1,
                            color = MaterialTheme.colors.primary
                        )
                        Button(
                            onClick = onClick,
                            colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.error),
                            shape = RoundedCornerShape(50)
                        ) {
                            Text(
                                text = errorButtonText.uppercase(),
                                style = MaterialTheme.typography.button
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun MyViewPreview() {
    ErrorCard(  modifier = Modifier
        .fillMaxWidth()
        .padding(top = 16.dp),
        errorTitle = AppStrings.error_title,
        errorDescription = "UNKNOWN_ERROR",
        errorButtonText = ErrorCardConsts.BUTTON_TEXT,
        onClick = {  },
        cardModifier = Modifier
            .fillMaxWidth()
            .height(LocalConfiguration.current.screenHeightDp.dp / 4 + 48.dp))
}