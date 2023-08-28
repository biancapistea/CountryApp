package com.example.countryapp.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.countryapp.ui.utils.toString
import com.example.domain.model.Country
import com.example.domain.usecase.LoadAllCountriesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(loadAllCountriesUseCase: LoadAllCountriesUseCase) :
    ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            loadAllCountriesUseCase.loadAllCountries().collectLatest { countries ->
                Log.d(
                    "I'm running on (loadAllCountriesByName from viewmodel)",
                    "${Thread.currentThread()}"
                )
                _uiState.update {
                    Log.d("country name is", countries.first().name.common)
                    val dateFormatted = getCurrentDate().toString("EEEE, MMMM dd")
                    it.copy(
                        countryName = countries.first().name.common,
                        currentDate = dateFormatted,
                        countries = countries
                    )
                }
            }
        }
    }

    private fun getCurrentDate() = Calendar.getInstance().time

    data class UiState(
        val countryName: String = "",
        val officialName: String = "",
        val currentDate: String = "",
        val countries: List<Country> = listOf()
    )
}