package com.example.countryapp.ui.connectivity

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.countryapp.R
import com.example.countryapp.ui.components.text.ParagraphTextComponent
import com.example.countryapp.ui.components.text.TitleText
import com.example.countryapp.ui.utils.currentConnectivityState
import com.example.countryapp.ui.utils.observeConnectivityAsFlow
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@Composable
fun connectivityState(): State<ConnectionState> {
    val context = LocalContext.current

    return produceState(initialValue = context.currentConnectivityState) {
        context.observeConnectivityAsFlow().collect { value = it }
    }
}

@ExperimentalCoroutinesApi
@Composable
fun ConnectivityStatus(screenContent: Unit) {
    val connection by connectivityState()

    val isConnected = connection === ConnectionState.Available

    if (!isConnected) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.White)
        ) {
            Image(
                modifier = Modifier
                    .height(460.dp)
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally),
                painter = painterResource(id = R.drawable.img_no_internet),
                contentDescription = null
            )
            TitleText(text = stringResource(R.string.oops_no_internet_connection))
            ParagraphTextComponent(
                text = stringResource(R.string.no_internet_description),
                paddingValues = PaddingValues(
                    start = 52.dp,
                    end = 52.dp,
                    bottom = 16.dp,
                    top = 16.dp
                ),
            )
        }
    } else {
        screenContent
    }
}