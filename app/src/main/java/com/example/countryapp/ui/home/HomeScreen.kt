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
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.countryapp.R
import com.example.countryapp.ui.components.items.HomeItem
import com.example.countryapp.ui.connectivity.ConnectivityStatus
import com.example.countryapp.ui.models.HomeSectionItem
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalCoroutinesApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(
    drawerState: DrawerState,
    onNavigateToDashboard: () -> Unit,
    onNavigateToLearnCountries: () -> Unit,
    onNavigateToPlayScreen: () -> Unit
) {
    ConnectivityStatus(
        HomeScreenContent(
            drawerState,
            onNavigateToDashboard,
            onNavigateToLearnCountries,
            onNavigateToPlayScreen
        )
    )
}


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun HomeScreenContent(
    drawerState: DrawerState,
    onNavigateToDashboard: () -> Unit,
    onNavigateToLearnCountries: () -> Unit,
    onNavigateToPlayScreen: () -> Unit
) {
    //TODO: sa ma gandesc cum pot muta lista  altundeva
    val images = listOf(
        HomeSectionItem(
            headerImage = R.drawable.img_discover_countries,
            description = "Would you like to test your knowledge about all the countries in the world? You can select the quiz type and answer the quiz questions to test your knowledge about the world's countries. You can also select from which region you want to receive questions.",
            actionText = "Go to quiz game",
            title = "Play & Discover new countries",
            onClickOnActionText = onNavigateToDashboard
        ),
        HomeSectionItem(
            headerImage = R.drawable.img_play_hangman_guess,
            description = "Do you feel like you know every capital or flag of all the countries in the world? Then, you can challenge yourself by playing the hangman game. You will need to guess the capital of the country or the country based on the flag. What do you say?",
            actionText = "Go to hangman game",
            title = "Play & Learn",
            onClickOnActionText = onNavigateToPlayScreen
        ),
        HomeSectionItem(
            headerImage = R.drawable.img_header_dashboard,
            description = "Do you feel like you need to learn/ find out more details about the countries in the world? You can try the Training Mode and then challenge yourself by taking the quiz or playing hangman!",
            actionText = "Go to training",
            title = "Learn & Train",
            onClickOnActionText = onNavigateToLearnCountries
        ),
    )

    val pageState = rememberPagerState { images.size }
    val coroutineScope = rememberCoroutineScope()

    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .zIndex(2f)
        ) {
            Image(painter = painterResource(R.drawable.ic_menu),
                contentDescription = null,
                modifier = Modifier
                    .padding(top = 24.dp, start = 12.dp)
                    .zIndex(2f)
                    .clickable {
                        coroutineScope.launch {
                            drawerState.open()
                        }
                    })
        }
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
