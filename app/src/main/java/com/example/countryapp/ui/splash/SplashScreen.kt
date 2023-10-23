package com.example.countryapp.ui.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.repeatOnLifecycle
import com.example.countryapp.R

@Composable
fun SplashScreen(onNavigateToDashboard: () -> Unit = {}, viewModel: SplashViewModel) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val isLoading by produceState(
        initialValue = true
    ) {
        lifecycle.repeatOnLifecycle(state = androidx.lifecycle.Lifecycle.State.STARTED) {
            viewModel.isLoading.collect { value = it }
        }
    }

    if (isLoading) {
        SplashScreenContent()
    } else {
        LaunchedEffect(Unit) {
            onNavigateToDashboard()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun SplashScreenContent() {
    Scaffold(
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(Color.White),
                contentAlignment = Alignment.Center,
                content = {
                    Image(
                        modifier = Modifier
                            .wrapContentSize(),
                        painter = painterResource(id = R.drawable.ic_geography_splash),
                        contentDescription = null
                    )
                }
            )
        }
    )
}