package com.example.countryapp.ui.game.choosegameplay

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.countryapp.R
import com.example.countryapp.ui.components.customgriditems.gridItems
import com.example.countryapp.ui.components.items.GameplayItem
import com.example.countryapp.ui.components.items.TopBackAppBar
import com.example.countryapp.ui.components.text.TitleText
import com.example.countryapp.ui.connectivity.ConnectivityStatus
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun ChooseGameplayScreen(
    viewModel: ChooseGameplayViewModel,
    onDashboardTypePressed: (GameplayType) -> Unit = {},
    onBackPressed: () -> Boolean
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    ConnectivityStatus {
        ChooseGameplayContent(
            uiState,
            onDashboardTypePressed,
            onBackPressed
        )
    }
}

@Composable
private fun ChooseGameplayContent(
    uiState: ChooseGameplayViewModel.UiState,
    onDashboardTypePressed: (GameplayType) -> Unit,
    onBackPressed: () -> Boolean
) {
    val listState = rememberLazyListState()
    TopBackAppBar(onPopBack = onBackPressed)
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
                    GameplayItem(itemData)
                }
            ) { position ->
                onDashboardTypePressed(uiState.listOfDashboardTypes[position].type)
            }
        }
    }
}