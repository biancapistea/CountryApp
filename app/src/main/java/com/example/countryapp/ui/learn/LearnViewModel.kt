package com.example.countryapp.ui.learn

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.countryapp.ui.models.Country
import com.example.countryapp.ui.models.CountryMapper
import com.example.countryapp.ui.quiz.selectregion.RegionQuizType
import com.example.domain.model.Resource
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
            loadAllCountriesUseCase.loadAllCountries().collectLatest { status ->
                when (status) {
                    is Resource.Success -> {
                        val countriesMapped =
                            status.data?.map { country -> CountryMapper.map(country) }

                        if (countriesMapped != null) {
                            _uiState.update {
                                it.copy(
                                    europeCountries = countriesMapped.filter { elem -> elem.region?.uppercase() == RegionQuizType.EUROPE.name },
                                    asiaCountries = countriesMapped.filter { elem -> elem.region?.uppercase() == RegionQuizType.ASIA.name },
                                    africaCountries = countriesMapped.filter { elem -> elem.region?.uppercase() == RegionQuizType.AFRICA.name },
                                    oceaniaCountries = countriesMapped.filter { elem -> elem.region?.uppercase() == RegionQuizType.OCEANIA.name },
                                    americasCountries = countriesMapped.filter { elem -> elem.region?.uppercase() == RegionQuizType.AMERICAS.name },
                                )
                            }
                        }
                    }

                    is Resource.Loading -> {

                    }

                    is Resource.Error -> {

                    }
                }
            }
        }
    }

    data class UiState(
        val europeCountries: List<Country> = listOf(),
        val asiaCountries: List<Country> = listOf(),
        val africaCountries: List<Country> = listOf(),
        val oceaniaCountries: List<Country> = listOf(),
        val americasCountries: List<Country> = listOf(),
    )
}