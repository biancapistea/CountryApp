package com.example.countryapp.ui.game.choosegameplay

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
class ChooseGameplayViewModel @Inject constructor() : ViewModel() {
    data class UiState(
        val headerImage: Int = R.drawable.img_hangman_game,
        val headerTitle: Int = R.string.empty,
        val headerSubline: Int = R.string.empty,
        val listOfDashboardTypes: List<GameplayUiContent> = listOf()
    )

    data class GameplayUiContent(
        val type: GameplayType = GameplayType.EMPTY,
        val teaserImage: Int = 0,
        val title: Int = 0
    )

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    headerImage = R.drawable.img_hangman_game,
                    headerTitle = R.string.play_and_learn_challenge,
                    headerSubline = R.string.choose_gameplay_hangman,
                    listOfDashboardTypes = listOf(
                        GameplayUiContent(
                            type = GameplayType.FREE_PLAY,
                            teaserImage = R.drawable.img_freeplay,
                            title = R.string.free_play
                        ),
                        GameplayUiContent(
                            type = GameplayType.LEVELS,
                            teaserImage = R.drawable.img_levels,
                            title = R.string.levels
                        )
                    )
                )
            }
        }
    }
}