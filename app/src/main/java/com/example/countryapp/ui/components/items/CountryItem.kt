package com.example.countryapp.ui.components.items

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.countryapp.R
import com.example.countryapp.ui.models.Country

@Composable
fun CountryItem(country: Country) {
    Box(
        modifier = Modifier.wrapContentSize()
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            if (country.flags?.png?.isNotEmpty() == true) {
                AsyncImage(
                    model = country.flags.png,
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .width(88.dp)
                        .wrapContentHeight()
                        .border(1.dp, Color.Gray)
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.ic_no_flag),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .width(88.dp)
                        .height(100.dp)
                        .border(1.dp, Color.Black)
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