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
    ConnectivityStatus(
        HomeScreenContent(
            onNavigateToDashboard,
            onNavigateToLearnCountries,
            onNavigateToPlayScreen
        )
    )
}


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreenContent(
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
        HomeSectionItem(
            headerImage = R.drawable.img_berlin,
            description = "You don't know where to travel next? Berlin, capital of Germany can be the best choice. Berlin has been the stage for a lot of world history and not just the fall of the Berlin Wall. You can still discover the traces of history in countless places around the capital. You can also explore traces of history in the modernist housing estates, which are UNESCO World Heritage Sites.",
            actionText = "Learn about Germany",
            title = "Visit Berlin",
            onClickOnActionText = onNavigateToLearnCountries //TODO: aici trebuie sa fac trimitere fix la pagina cu berlin!!
        ),
        HomeSectionItem(
            headerImage = R.drawable.img_rome,
            description = "You don't know where to travel next? Rome, capital of Italy can be the best choice. Visiting Rome is a life-changing experience thanks to its history, monuments and beauty. The centre boasts legendary sites such as the Colosseum, the Fori Imperiali, the Pantheon or the Fountain of Trevi, but also charming neighbourhoods where you can breathe all the authentic Romanness, full of local culture and good food. Without forgetting about the Vatican and Piazza San Pietro, among the most visited religious destinations in the world.",
            actionText = "Learn about Italy",
            title = "Visit Rome",
            onClickOnActionText = onNavigateToLearnCountries //TODO: aici trebuie sa fac trimitere fix la pagina cu berlin!!
        ),
        HomeSectionItem(
            headerImage = R.drawable.img_bangkok,
            description = "Bangkok is the most visited city in the world. It's the jumping-off point for many trips to Thailand and the rest of Southeast Asia, with an important international airport, but there are so many more reasons to visit. Lively traditional markets and exquisite golden temples - where you'll be immersed in the daily life of locals as it has existed for centuries - are found side by side with soaring glass skyscrapers, fashionable rooftop bars, and immense modern malls. ",
            actionText = "Learn about Thailand",
            title = "Visit Bangkok",
            onClickOnActionText = onNavigateToLearnCountries //TODO: aici trebuie sa fac trimitere fix la pagina cu berlin!!
        ),
        HomeSectionItem(
            headerImage = R.drawable.img_dubai,
            description = "Beautiful beaches, record-breaking attractions and experiences like no other – Dubai is the place to be in 2023. It's no wonder it has been named Tripadvisor's #1 Most Popular Destination in the World for the second year running. Let's explore!",
            actionText = "Learn about United Arab Emirates",
            title = "Visit Dubai",
            onClickOnActionText = onNavigateToLearnCountries //TODO: aici trebuie sa fac trimitere fix la pagina cu berlin!!
        ),
    )

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
