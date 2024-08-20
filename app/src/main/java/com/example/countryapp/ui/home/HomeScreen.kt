package com.example.countryapp.ui.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.countryapp.R
import com.example.countryapp.ui.components.items.HomeItem
import com.example.countryapp.ui.connectivity.ConnectivityStatus
import com.example.countryapp.ui.models.HomeSectionItem
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(
    onNavigateToDashboard: () -> Unit,
    onNavigateToLearnCountries: () -> Unit,
    onNavigateToPlayScreen: () -> Unit
) {
    ConnectivityStatus {
        HomeScreenContent(
            onNavigateToDashboard,
            onNavigateToLearnCountries,
            onNavigateToPlayScreen
        )
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreenContent(
    onNavigateToDashboard: () -> Unit,
    onNavigateToLearnCountries: () -> Unit,
    onNavigateToPlayScreen: () -> Unit
) {
    val images = HomeScreenData.getImages(onNavigateToDashboard, onNavigateToLearnCountries, onNavigateToPlayScreen)
    val pageState = rememberPagerState { images.size }
    val coroutineScope = rememberCoroutineScope()

    Box(modifier = Modifier.fillMaxSize()) {
        HorizontalPager(state = pageState) { pageNumber ->
            Box {
                HomeItem(
                    headerImage = images[pageNumber].headerImage,
                    description = images[pageNumber].description,
                    actionText = images[pageNumber].actionText,
                    title = images[pageNumber].title,
                    onClickOnActionText = images[pageNumber].onClickOnActionText
                )
                Row(
                    Modifier
                        .wrapContentHeight()
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter),
                    horizontalArrangement = Arrangement.Center
                ) {
                    repeat(images.size) { iteration ->
                        val dot =
                            if (pageState.currentPage == iteration) R.drawable.ic_white_dot_selected else R.drawable.ic_white_dot_unselected
                        Image(
                            painter = painterResource(dot),
                            contentDescription = null,
                            modifier = Modifier
                                .size(26.dp)
                                .clickable {
                                    coroutineScope.launch {
                                        pageState.animateScrollToPage(iteration)
                                    }
                                }
                                .padding(bottom = 16.dp, end = 8.dp),
                        )
                    }
                }
            }
        }

        LaunchedEffect(pageState.currentPage) {
            delay(5000)
            coroutineScope.launch {
                var newPosition = pageState.currentPage + 1
                if (newPosition > images.size - 1) {
                    newPosition = 0
                }
                pageState.animateScrollToPage(newPosition)
            }
        }

    }
}
