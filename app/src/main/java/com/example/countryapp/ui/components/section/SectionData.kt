package com.example.countryapp.ui.components.section

import com.example.countryapp.ui.models.Country

data class SectionData(
    val headerText: String,
    val headerImage: Int = 0,
    val items: List<Country>,
    val onItemClick: (Int) -> Unit
)
