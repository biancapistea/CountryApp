package com.example.countryapp.ui.learn.countrydetails

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.countryapp.ui.models.Country

@Composable
fun CountryDetailsScreen(country: Country) {
    Column {
        Text(text = country.population.toString())
    }
}