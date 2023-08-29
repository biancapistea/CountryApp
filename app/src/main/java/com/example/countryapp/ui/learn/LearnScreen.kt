package com.example.countryapp.ui.learn

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import coil.compose.AsyncImage
import com.example.countryapp.R
import com.example.domain.model.Country

@Composable
fun LearnScreen(viewModel: LearnViewModel, onCountryClick: (Int) -> Unit) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val uiState by produceState(
        initialValue = LearnViewModel.UiState()
    ) {
        lifecycle.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
            viewModel.uiState.collect { value = it }
        }
    }

    LearnCountriesList(uiState, onCountryClick)
}

@Composable
private fun LearnCountriesList(uiState: LearnViewModel.UiState, onBadgeClick: (Int) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize()
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 40.dp, start = 16.dp, end = 16.dp, top = 16.dp)
        ) {
            item {
                Text(
                    text = "Select a country and find more details about it!",
                    color = Color.Black,
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.graphik_regular)),
                    style = MaterialTheme.typography.titleLarge.copy(lineHeight = 22.sp),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp)
                        .align(alignment = Alignment.CenterHorizontally)
                )
            }

            gridItems(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(
                    end = 16.dp,
                    start = 16.dp,
                    top = 16.dp,
                    bottom = 16.dp
                ),
                data = uiState.countries,
                columnCount = 3,
                onBadgeClick = { position ->
                        onBadgeClick(position)
                },
            ) { itemData ->
                CountryItem(itemData)
            }
        }
    }
}

@Composable
fun CountryItem(country: Country) {
    Box(
        modifier = Modifier.wrapContentSize()
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            if(country.flags?.png?.isNotEmpty() == true) {
                AsyncImage(
                    model = country.flags?.png ?: "",
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .width(88.dp)
                        .height(100.dp)
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.ic_no_flag),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .width(88.dp)
                        .height(100.dp)
                )
            }
            Text(
                text = country.name.common,
                color = Color.Black,
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.graphik_regular)),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge.copy(lineHeight = 22.sp),
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth()
            )
        }
    }
}

fun <T> LazyListScope.gridItems(
    data: List<T>,
    columnCount: Int,
    modifier: Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    onBadgeClick: (Int) -> Unit,
    itemContent: @Composable BoxScope.(T) -> Unit,
) {
    val size = data.count()
    val rows = if (size == 0) 0 else 1 + (size - 1) / columnCount
    items(rows, key = { it.hashCode() }) { rowIndex ->
        Row(
            horizontalArrangement = horizontalArrangement, modifier = modifier
        ) {
            for (columnIndex in 0 until columnCount) {
                val itemIndex = rowIndex * columnCount + columnIndex
                if (itemIndex < size) {
                    Box(
                        modifier = Modifier
                            .weight(1F, fill = true)
                            .clickable(indication = null,
                                interactionSource = remember { MutableInteractionSource() }) {
                                onBadgeClick(itemIndex)
                            },
                        propagateMinConstraints = true
                    ) {
                        itemContent(data[itemIndex])
                    }
                } else {
                    Spacer(Modifier.weight(1F, fill = true))
                }
            }
        }
    }
}