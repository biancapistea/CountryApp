package com.example.countryapp.ui.models

data class HomeSectionItem(
    val headerImage: Int,
    val description: String,
    val actionText: String,
    val title: String,
    val onClickOnActionText: () -> Unit
)