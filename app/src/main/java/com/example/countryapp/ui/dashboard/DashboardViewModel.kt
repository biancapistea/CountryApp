package com.example.countryapp.ui.dashboard

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
class DashboardViewModel @Inject constructor() :
    ViewModel() {
    data class UiState(
        val headerImage: Int = R.drawable.image_country_flags_header,
        val headerTitle: Int = R.string.empty,
        val headerSubline: Int = R.string.empty,
        val listOfDashboardTypes: List<DashboardUiType> = listOf()
    )

    data class DashboardUiType(
        val type: DashboardQuizType = DashboardQuizType.EMPTY,
        val teaserImage: Int = 0,
        val title: Int = 0
    )

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    headerImage = R.drawable.image_country_flags_header,
                    headerTitle = R.string.world_s_countries_challenge,
                    headerSubline = R.string.dashboard_header_subline,
                    listOfDashboardTypes = listOf(
                        DashboardUiType(
                            type = DashboardQuizType.FLAGS,
                            teaserImage = R.drawable.image_worlds_flags,
                            title = R.string.flags
                        ),
                        DashboardUiType(
                            type = DashboardQuizType.CAPITALS,
                            teaserImage = R.drawable.image_worlds_capitals,
                            title = R.string.capitals
                        ),
                        DashboardUiType(
                            type = DashboardQuizType.GENERAL_ASPECTS,
                            teaserImage = R.drawable.img_general_aspects_quiz,
                            title = R.string.general_aspects
                        ),
                        DashboardUiType(
                            type = DashboardQuizType.COAT_OF_ARMS,
                            teaserImage = R.drawable.ic_coat_of_arms,
                            title = R.string.coat_of_arms
                        )
                    )
                )
            }
        }
    }
}