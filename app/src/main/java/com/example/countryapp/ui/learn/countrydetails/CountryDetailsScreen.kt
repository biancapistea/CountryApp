package com.example.countryapp.ui.learn.countrydetails

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage
import com.example.countryapp.R
import com.example.countryapp.ui.components.text.ParagraphTextComponent
import com.example.countryapp.ui.components.text.TitleText
import com.example.countryapp.ui.learn.LearnViewModel
import com.example.countryapp.ui.models.Country

@Composable
fun CountryDetailsScreen(
    viewModel: CountryDetailsViewModel,
    country: Country,
    onBackPressed: () -> Unit
) {
    CountryDetails(country, onBackPressed, viewModel::formatCapitals, viewModel::formatCapitalText)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CountryDetails(
    country: Country,
    onBackPressed: () -> Unit,
    formatCapitals: (List<String>) -> String,
    formatCapitalText: (Int) -> String,
) {
    Scaffold(
        content = { paddingValues ->
            Box(modifier = Modifier.background(Color.White)) {
                Column(
                    modifier = Modifier
                        .wrapContentSize()
                        .zIndex(2f)
                        .align(Alignment.TopStart)
                        .padding(top = 24.dp, start = 24.dp)
                        .clickable { onBackPressed() },
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_back_button_black),
                        modifier = Modifier
                            .height(48.dp),
                        contentDescription = null
                    )
                }
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = paddingValues.calculateBottomPadding())
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        Modifier
                            .fillMaxWidth()
                    ) {
                        Box(modifier = Modifier.fillMaxWidth()) {
                            AsyncImage(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(9f / 5f),
                                contentScale = ContentScale.Crop,
                                model = country.flags?.png,
                                contentDescription = null
                            )
                        }
                        Divider(
                            color = Color.Black,
                            modifier = Modifier
                                .height(1.dp)
                                .fillMaxWidth()
                        )
                        TitleText(
                            modifier = Modifier.padding(
                                top = 32.dp,
                                bottom = 24.dp,
                                end = 36.dp,
                                start = 36.dp
                            ),
                            text = country.name.common
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 24.dp)
                        ) {
                            if (country.coatOfArms?.png != null) {
                                AsyncImage(
                                    modifier = Modifier
                                        .height(160.dp)
                                        .width(160.dp)
                                        .align(Alignment.Center),
                                    contentScale = ContentScale.Fit,
                                    model = country.coatOfArms.png,
                                    contentDescription = null
                                )
                            } else {
                                Image(
                                    modifier = Modifier
                                        .height(160.dp)
                                        .width(160.dp)
                                        .align(Alignment.Center),
                                    contentScale = ContentScale.Fit,
                                    painter = painterResource(id = R.drawable.ic_no_coat_of_arms),
                                    contentDescription = null
                                )
                            }
                        }
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .shadow(
                                    10.dp, RoundedCornerShape(
                                        topStart = 24.dp,
                                        topEnd = 24.dp
                                    )
                                )
                                .clip(
                                    shape = RoundedCornerShape(
                                        topStart = 16.dp,
                                        topEnd = 16.dp
                                    )
                                ),
                            color = colorResource(id = R.color.red)
                        ) {
                            Column(
                                modifier = Modifier
                                    .padding(top = 32.dp)
                                    .align(Alignment.CenterHorizontally)
                            ) {
                                TitleText(
                                    modifier = Modifier
                                        .padding(bottom = 32.dp)
                                        .fillMaxWidth(),
                                    text = "General Aspects",
                                    color = Color.White
                                )
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(IntrinsicSize.Min)
                                        .padding(
                                            top = 16.dp,
                                            bottom = 40.dp,
                                            end = 24.dp,
                                            start = 24.dp
                                        )
                                        .align(Alignment.CenterHorizontally)
                                        .horizontalScroll(rememberScrollState())
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .padding(start = 32.dp, top = 6.dp, bottom = 6.dp)
                                            .align(Alignment.CenterStart)
                                    ) {
                                        ParagraphTextComponent(
                                            paddingValues = PaddingValues(bottom = 8.dp),
                                            text = country.capital?.let { formatCapitalText(it.size) }
                                                ?: "Capital: ",
                                            textAlign = TextAlign.Start,
                                            color = Color.White
                                        )
                                        ParagraphTextComponent(
                                            paddingValues = PaddingValues(bottom = 8.dp),
                                            text = "Population: ",
                                            textAlign = TextAlign.Start,
                                            color = Color.White
                                        )
                                        ParagraphTextComponent(
                                            paddingValues = PaddingValues(bottom = 8.dp),
                                            text = "Region: ",
                                            textAlign = TextAlign.Start,
                                            color = Color.White
                                        )
                                        ParagraphTextComponent(
                                            paddingValues = PaddingValues(bottom = 8.dp),
                                            text = "Subregion: ",
                                            textAlign = TextAlign.Start,
                                            color = Color.White
                                        )
                                        ParagraphTextComponent(
                                            paddingValues = PaddingValues(bottom = 8.dp),
                                            text = "Is Independent: ",
                                            textAlign = TextAlign.Start,
                                            color = Color.White
                                        )
                                    }
                                    VerticalDivider(modifier = Modifier.align(Alignment.Center))
                                    Column(
                                        modifier = Modifier
                                            .padding(end = 32.dp, top = 6.dp, bottom = 6.dp)
                                            .align(Alignment.CenterEnd)
                                    ) {
                                        ParagraphTextComponent(
                                            paddingValues = PaddingValues(bottom = 8.dp),
                                            text = country.capital?.let { formatCapitals(it) }
                                                ?: "Does not have",
                                            textAlign = TextAlign.Start,
                                            color = Color.White
                                        )
                                        ParagraphTextComponent(
                                            paddingValues = PaddingValues(bottom = 8.dp),
                                            text = country.population.toString(),
                                            textAlign = TextAlign.Start,
                                            color = Color.White
                                        )
                                        ParagraphTextComponent(
                                            paddingValues = PaddingValues(bottom = 8.dp),
                                            text = country.region.toString(),
                                            textAlign = TextAlign.Start,
                                            color = Color.White
                                        )
                                        ParagraphTextComponent(
                                            paddingValues = PaddingValues(bottom = 8.dp),
                                            text = country.subregion.toString(),
                                            textAlign = TextAlign.Start,
                                            color = Color.White
                                        )
                                        ParagraphTextComponent(
                                            paddingValues = PaddingValues(bottom = 8.dp),
                                            text = if (country.independent) "Yes" else "No",
                                            textAlign = TextAlign.Start,
                                            color = Color.White
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun VerticalDivider(
    modifier: Modifier = Modifier,
    color: Color = Color.White,
    thickness: Dp = 1.dp,
) {
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .width(thickness)
            .padding(vertical = 4.dp)
            .background(color = color)
            .then(modifier)
    )
}
