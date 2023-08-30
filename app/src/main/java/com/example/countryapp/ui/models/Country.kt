package com.example.countryapp.ui.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Country(
    val name: Name,
    val flags: ImageData? = null,
    val coatOfArms: ImageData? = null,
    val capital: List<String>? = listOf(),
    val region: String? = "",
    val population: String? = "",
    val subregion: String? = "",
    val independent: Boolean = false
) : Parcelable

@Parcelize
data class Name(
    val common: String,
    val official: String
) : Parcelable

@Parcelize
data class ImageData(
    val png: String?,
    val svg: String?
) : Parcelable
