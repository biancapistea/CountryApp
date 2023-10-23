package com.example.countryapp.ui.game

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.example.countryapp.R
import com.example.countryapp.ui.components.customgriditems.gridItems
import com.example.countryapp.ui.components.items.DashboardItem
import com.example.countryapp.ui.components.text.TitleText
import com.example.countryapp.ui.connectivity.ConnectivityStatus
import com.example.countryapp.ui.dashboard.DashboardQuizType
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun GameDashboardScreen(
    viewModel: GameDashboardViewModel,
    onDashboardTypePressed: (DashboardQuizType) -> Unit = {}
) {

    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val uiState by produceState(
        initialValue = GameDashboardViewModel.UiState()
    ) {
        lifecycle.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
            viewModel.uiState.collect { value = it }
        }
    }

    ConnectivityStatus {
        GameDashboardContent(
            uiState,
            onDashboardTypePressed
        )
    }
}

@Composable
private fun GameDashboardContent(
    uiState: GameDashboardViewModel.UiState,
    onDashboardTypePressed: (DashboardQuizType) -> Unit,
) {
    val listState = rememberLazyListState()
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize()
            .background(Color.White)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 24.dp),
            state = listState
        ) {
            item {
                Box(
                    modifier = Modifier.wrapContentSize()
                ) {
                    Image(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White)
                            .height(260.dp),
                        contentScale = ContentScale.Crop,
                        painter = painterResource(id = uiState.headerImage),
                        contentDescription = null
                    )
                }
            }

            item {
                TitleText(
                    modifier = Modifier.padding(top = 40.dp, start = 40.dp, end = 40.dp),
                    text = stringResource(id = uiState.headerTitle),
                    style = MaterialTheme.typography.titleLarge.copy(lineHeight = 34.sp),
                    color = Color.Red,
                    fontWeight = FontWeight.Normal
                )
                Text(
                    text = stringResource(id = uiState.headerSubline),
                    color = Color.Black,
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.graphik_regular)),
                    textAlign = TextAlign.Center,
                    lineHeight = 22.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 40.dp, start = 36.dp, end = 36.dp)
                        .align(alignment = Alignment.CenterHorizontally)
                )
            }

            gridItems(
                horizontalArrangement = Arrangement.spacedBy(
                    24.dp,
                    alignment = Alignment.CenterHorizontally
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(top = 24.dp),
                data = uiState.listOfDashboardTypes,
                columnCount = 2,
                itemContent = { itemData ->
                    DashboardItem(itemData)
                }
            ) { position ->
                onDashboardTypePressed(uiState.listOfDashboardTypes[position].type)
            }
        }
    }
}
