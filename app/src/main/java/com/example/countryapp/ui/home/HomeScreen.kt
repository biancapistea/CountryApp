package com.example.countryapp.ui.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.example.countryapp.R
import com.example.countryapp.ui.components.buttons.BlueButtonComponent
import com.example.countryapp.ui.components.text.TitleText
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    drawerState: DrawerState,
    onNavigateToDashboard: () -> Unit,
    onNavigateToLearnCountries: () -> Unit
) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val uiState by produceState(
        initialValue = HomeViewModel.UiState()
    ) {
        lifecycle.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
            viewModel.uiState.collect { value = it }
        }
    }

    HomeScreenContent(uiState, drawerState, onNavigateToDashboard, onNavigateToLearnCountries)
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenContent(
    uiState: HomeViewModel.UiState,
    drawerState: DrawerState,
    onNavigateToDashboard: () -> Unit,
    onNavigateToLearnCountries: () -> Unit
) {
    Box {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(R.drawable.img_header_dashboard),
            contentDescription = "background_image",
            contentScale = ContentScale.FillBounds
        )
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                val coroutineScope = rememberCoroutineScope()
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                ) {
                    TopAppBar(
                        modifier = Modifier.align(Alignment.TopEnd),
                        title = {},
                        navigationIcon = {
                            IconButton(onClick = {
                                coroutineScope.launch {
                                    drawerState.open() //TODO: change this to be at top level of the application not only on home screen.
                                    //TODO: On dashboard also and on other screens as well
                                }
                            }) {
                                Image(
                                    painter = painterResource(R.drawable.ic_menu),
                                    contentDescription = null
                                )
                            }
                        },
                    )
                }
                Column(
                    Modifier
                        .wrapContentSize()
                        .padding(top = 52.dp)
                ) {
                    TitleText(text = stringResource(R.string.app_name))
                }
            },
            content = { paddingValues ->
                Column(
                    Modifier
                        .fillMaxSize()
                        .background(Color.White)
                        .padding(paddingValues)
                        .verticalScroll(rememberScrollState())
                ) {
                    BlueButtonComponent(
                        paddingValues = PaddingValues(
                            top = 52.dp,
                            start = 24.dp,
                            end = 24.dp
                        ), text = "Test your knowledge", onClick = { onNavigateToDashboard() })
                    BlueButtonComponent(
                        paddingValues = PaddingValues(
                            top = 24.dp,
                            start = 24.dp,
                            end = 24.dp
                        ),
                        text = "Learn and train",
                        onClick = { onNavigateToLearnCountries() })
                }
            }
        )
    }
}
