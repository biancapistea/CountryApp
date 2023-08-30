package com.example.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CountryDto(
    @Json(name = "name")
    val name: NameDto,
    @Json(name = "flags")
    val flags: ImageDataDto? = null,
    @Json(name = "coatOfArms")
    val coatOfArms: ImageDataDto? = null,
    @Json(name = "capital")
    val capital: List<String>? = listOf(),
    @Json(name = "region")
    val region: String? = "",
    @Json(name = "subregion")
    val subregion: String? = "",
    @Json(name = "population")
    val population: String? = "",
    @Json(name = "independent")
    val independent: Boolean? = true

)

@JsonClass(generateAdapter = true)
data class ImageDataDto(
    @Json(name = "png")
    val png: String?,
    @Json(name = "svg")
    val svg: String?
)

@JsonClass(generateAdapter = true)
data class NameDto(
    @Json(name = "common")
    val common: String,
    @Json(name = "official")
    val official: String,
)