package com.example.domain.model

data class Country(
    val name: Name,
    val flags: ImageData? = null,
    val coatOfArms: ImageData? = null,
    val capital: List<String>? = listOf(),
    val region: String? = "",
    val population: String? = "",
    val subregion: String? = "",
    val independent: Boolean? = true
)

data class Name(
    val common: String,
    val official: String
)

data class ImageData(
    val png: String?,
    val svg: String?
)
