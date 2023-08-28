package com.example.countryapp.ui.models

data class Quiz(
    val countryName: String = "",
    val correctAnswer: String = "",
    val answers: List<String> = listOf()
)