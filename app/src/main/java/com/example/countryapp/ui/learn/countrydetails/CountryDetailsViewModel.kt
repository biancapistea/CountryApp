package com.example.countryapp.ui.learn.countrydetails

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CountryDetailsViewModel @Inject constructor(): ViewModel() {
    fun formatCapitals(capitals: List<String>): String =
        if (capitals.size == 1) {
            capitals.first()
        } else {
            capitals.joinToString("\n")
        }

    fun formatCapitalText(capitalsSize: Int): String {
        var capital = "Capital: "
        if (capitalsSize == 1) return capital
        for (index in 1 until capitalsSize) {
            capital += "\n"
        }
        return capital
    }
}