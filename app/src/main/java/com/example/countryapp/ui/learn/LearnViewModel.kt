package com.example.countryapp.ui.learn

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.countryapp.ui.models.Country
import com.example.countryapp.ui.models.CountryMapper
import com.example.domain.usecase.LoadAllCountriesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LearnViewModel @Inject constructor(loadAllCountriesUseCase: LoadAllCountriesUseCase) :
    ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            loadAllCountriesUseCase.loadAllCountries().collectLatest { countries ->
                Log.d("countries returned by server", countries.toString())
                _uiState.update {
                    it.copy(
                        countries = countries.map { country -> CountryMapper.map(country) }
                    )
                }
            }
        }
    }
    
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

    data class UiState(
        val countries: List<Country> = listOf()
    )
}