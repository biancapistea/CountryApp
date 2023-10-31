package com.example.countryapp.ui.game.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.countryapp.R
import com.example.countryapp.ui.dashboard.DashboardQuizType
import com.example.countryapp.ui.dashboard.DashboardViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HangmanGameDashboardViewModel @Inject constructor() : ViewModel() {
    data class UiState(
        val headerImage: Int = R.drawable.img_hangman_game,
        val headerTitle: Int = R.string.empty,
        val headerSubline: Int = R.string.empty,
        val listOfDashboardTypes: List<DashboardViewModel.DashboardUiType> = listOf()
    )


    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    headerImage = R.drawable.img_hangman_game,
                    headerTitle = R.string.play_and_learn_challenge,
                    headerSubline = R.string.choose_type_hangman,
                    listOfDashboardTypes = listOf(
                        DashboardViewModel.DashboardUiType(
                            type = DashboardQuizType.FLAGS,
                            teaserImage = R.drawable.image_worlds_flags,
                            title = R.string.flags
                        ),
                        DashboardViewModel.DashboardUiType(
                            type = DashboardQuizType.CAPITALS,
                            teaserImage = R.drawable.image_worlds_capitals,
                            title = R.string.capitals
                        ),
                        DashboardViewModel.DashboardUiType(
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