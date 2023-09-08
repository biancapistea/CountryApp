package com.example.countryapp.ui.learn

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.example.countryapp.R
import com.example.countryapp.ui.components.section.ExpandableList
import com.example.countryapp.ui.components.section.SectionData
import com.example.countryapp.ui.models.Country
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LearnScreen(
    viewModel: LearnViewModel,
    onCountryClick: (Country) -> Unit,
    drawerState: DrawerState
) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val uiState by produceState(
        initialValue = LearnViewModel.UiState()
    ) {
        lifecycle.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
            viewModel.uiState.collect { value = it }
        }
    }

    LearnCountriesList(uiState, onCountryClick, drawerState)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LearnCountriesList(
    uiState: LearnViewModel.UiState,
    onCountryClick: (Country) -> Unit,
    drawerState: DrawerState
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize()
            .background(Color.White)
    ) {
        val coroutineScope = rememberCoroutineScope()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 40.dp, start = 16.dp, end = 16.dp, top = 16.dp),
        ) {
            Box(
                modifier = Modifier.wrapContentSize()
            ) {
                Image(painter = painterResource(R.drawable.ic_menu),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .zIndex(2f)
                        .clickable {
                            coroutineScope.launch {
                                drawerState.open()
                            }
                        })
            }
            ExpandableList(
                sections = listOf(
                    SectionData(
                        headerText = stringResource(id = R.string.europe),
                        items = uiState.europeCountries,
                        onItemClick = { position ->
                            onCountryClick(uiState.europeCountries[position])
                        },
                        headerImage = R.drawable.img_europe_continent
                    ),
                    SectionData(
                        headerText = stringResource(id = R.string.asia),
                        items = uiState.asiaCountries,
                        onItemClick = { position ->
                            onCountryClick(uiState.asiaCountries[position])
                        },
                        headerImage = R.drawable.img_asia_continent
                    ),
                    SectionData(
                        headerText = stringResource(id = R.string.africa),
                        items = uiState.africaCountries,
                        onItemClick = { position ->
                            onCountryClick(uiState.africaCountries[position])
                        },
                        headerImage = R.drawable.img_africa_continent
                    ),
                    SectionData(
                        headerText = stringResource(R.string.oceania),
                        items = uiState.oceaniaCountries,
                        onItemClick = { position ->
                            onCountryClick(uiState.oceaniaCountries[position])
                        },
                        headerImage = R.drawable.img_oceania_continent
                    ),
                    SectionData(
                        headerText = stringResource(R.string.americas),
                        items = uiState.americasCountries,
                        onItemClick = { position ->
                            onCountryClick(uiState.americasCountries[position])
                        },
                        headerImage = R.drawable.img_americas_continent
                    )
                ),
                explainingMessage = stringResource(R.string.learn_and_train_message)
            )
        }
    }
}