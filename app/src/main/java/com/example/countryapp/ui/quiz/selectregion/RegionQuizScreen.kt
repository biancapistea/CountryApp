package com.example.countryapp.ui.quiz.selectregion

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.example.countryapp.R
import com.example.countryapp.ui.components.customgriditems.gridItems
import com.example.countryapp.ui.components.items.RegionItem

@Composable
fun RegionQuizScreen(
    viewModel: RegionQuizViewModel,
    onRegionTypePressed: (RegionQuizType) -> Unit,
    onBackPressed: () -> Unit
) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val uiState by produceState(
        initialValue = RegionQuizViewModel.UiState()
    ) {
        lifecycle.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
            viewModel.uiState.collect { value = it }
        }
    }

    RegionQuizContent(uiState, onRegionTypePressed, onBackPressed)
}

@Composable
private fun RegionQuizContent(
    uiState: RegionQuizViewModel.UiState,
    onRegionTypePressed: (RegionQuizType) -> Unit,
    onBackPressed: () -> Unit
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
                Image(
                    painter = painterResource(id = R.drawable.ic_back),
                    modifier = Modifier
                        .padding(top = 24.dp, start = 24.dp).height(48.dp).clickable { onBackPressed() },
                    contentDescription = null
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
                        .padding(top = 10.dp, start = 36.dp, end = 36.dp)
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
                data = uiState.listOfRegionTypes,
                columnCount = 2,
                itemContent = { itemData ->
                    RegionItem(itemData)
                }
            ) { position ->
                onRegionTypePressed(uiState.listOfRegionTypes[position].type)
            }
        }
    }
}