package com.example.countryapp.ui.quiz.selectregion

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.countryapp.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegionQuizViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    data class UiState(
        val headerTitle: Int = R.string.empty,
        val headerSubline: Int = R.string.empty,
        val listOfRegionTypes: List<RegionUiType> = listOf()
    )

    data class RegionUiType(
        val type: RegionQuizType = RegionQuizType.ALL,
        val teaserImage: Int = 0,
        val title: String = ""
    )

    init {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    headerSubline = R.string.region_quiz_subline,
                    listOfRegionTypes = listOf(
                        RegionUiType(
                            type = RegionQuizType.ALL,
                            teaserImage = R.drawable.img_all_continents,
                            title = "All continents"
                        ),
                        RegionUiType(
                            type = RegionQuizType.AFRICA,
                            teaserImage = R.drawable.img_africa_continent,
                            title = "Africa"
                        ),
                        RegionUiType(
                            type = RegionQuizType.EUROPE,
                            teaserImage = R.drawable.img_europe_continent,
                            title = "Europe"
                        ),
                        RegionUiType(
                            type = RegionQuizType.ASIA,
                            teaserImage = R.drawable.img_asia_continent,
                            title = "Asia"
                        ),
                        RegionUiType(
                            type = RegionQuizType.OCEANIA,
                            teaserImage = R.drawable.img_oceania_continent,
                            title = "Oceania"
                        ),
                        RegionUiType(
                            type = RegionQuizType.AMERICAS,
                            teaserImage = R.drawable.img_americas_continent,
                            title = "Americas"
                        )
                    )
                )
            }
        }
    }
}