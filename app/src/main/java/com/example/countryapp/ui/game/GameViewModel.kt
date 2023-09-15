package com.example.countryapp.ui.game

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.countryapp.ui.dashboard.DashboardQuizType
import com.example.countryapp.ui.quiz.GeneralAspectsQuizQuestions
import com.example.countryapp.ui.utils.StringUtil
import com.example.domain.model.Country
import com.example.domain.usecase.LoadAllCountriesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.Collator
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class GameViewModel @Inject constructor(
    loadAllCountriesUseCase: LoadAllCountriesUseCase,
    savedStateHandle: SavedStateHandle
) :
    ViewModel() {
    private val _uiState = MutableStateFlow(GameUiState())
    val uiState = _uiState.asStateFlow()

    private val dashboardType: String = checkNotNull(savedStateHandle["dashboardType"])
    private var lettersGuessed: MutableSet<Char> = mutableSetOf()
    private var correctLetters: MutableSet<Char> = mutableSetOf()
    private var wrongLetters: MutableSet<Char> = mutableSetOf()

    private var _currentLetterGuessed: Char = ' '
    val isGameOver: Boolean get() = _uiState.value.livesLeft == 0

    private var _currentStreakCount: Int = 0

    init {
        viewModelScope.launch {
            loadAllCountriesUseCase.loadAllCountries().collectLatest { countries ->
                _uiState.update {
                    it.copy(
                        countries = countries,
                        wordRandomlyChosen = getRandomWordByType(countries, dashboardType),
                        question = getQuestionByType(dashboardType),
                        dashboardType = dashboardType
                    )
                }
            }
        }
    }

    private fun getQuestionByType(dashboardType: String): String {
        return when (dashboardType) {
            DashboardQuizType.FLAGS.name -> {
                "What country's flag is showed below?"
            }

            DashboardQuizType.CAPITALS.name -> {
                "Capital of ${uiState.value.countryRandomChosen}"
            }

            DashboardQuizType.COAT_OF_ARMS.name -> {
                "What country's coat of arms is showed below?"
            }

            else -> {
                ""
            }
        }
    }

    private fun getRandomWordByType(
        countries: List<Country>,
        dashboardType: String
    ): String {
        val randomCountryIndex = Random.nextInt(0, countries.size - 1)
        val randomCountry = countries[randomCountryIndex].name.common

        return when (dashboardType) {
            DashboardQuizType.CAPITALS.name -> {
                _uiState.update {
                    it.copy(
                        countryRandomChosen = randomCountry,
                        tipText = "If you think this country does not have a capital, write: Does not have"
                    )
                }
                countries[randomCountryIndex].capital?.firstOrNull()?.uppercase() ?: ""
            }

            DashboardQuizType.FLAGS.name -> {
                _uiState.update {
                    it.copy(
                        countryRandomFlag = countries[randomCountryIndex].flags?.png ?: "",
                        countryRandomChosen = randomCountry,
                        tipText = "It is a country from ${countries[randomCountryIndex].region}"
                    )
                }
                countries[randomCountryIndex].name.common.uppercase()
            }

            else -> {
                ""
            }
        }
    }

    private fun isLetterGuessCorrect(letterFromButton: Char): Boolean? {
        val collator: Collator = Collator.getInstance()
        collator.strength = Collator.PRIMARY

        val wordRandomlyChosen = _uiState.value.wordRandomlyChosen
        val normalizedLetterFromButton = letterFromButton.toString()

        return wordRandomlyChosen?.any { char ->
            collator.compare(char.toString(), normalizedLetterFromButton) == 0
        }
    }

    fun checkUserGuess(letterFromButton: Char) {
        lettersGuessed.add(letterFromButton)
        _currentLetterGuessed = letterFromButton.uppercaseChar()

        if (isLetterGuessCorrect(letterFromButton) == true) {
            correctLetters.add(_currentLetterGuessed)
            _uiState.update { currentState ->
                currentState.copy(
                    usedLetters = lettersGuessed.toSet(),
                    correctLetters = correctLetters.toSet(),
                )
            }
        } else {
            wrongLetters.add(_currentLetterGuessed)

            _uiState.update { currentState ->
                currentState.copy(
                    usedLetters = lettersGuessed.toSet(),
                    wrongLetters = wrongLetters.toSet(),
                    livesLeft = currentState.livesLeft.dec(),
                )
            }
        }
    }

    fun isWordCorrectlyGuessed(): Boolean {
        return if (_uiState.value.wordRandomlyChosen != "") {
            _uiState.value.wordRandomlyChosen?.run {
                val wordWithoutWhitespaces =
                    StringUtil.removeWhitespacesAndHyphens(this).uppercase()
                val normalizedWord = StringUtil.normalizeWord(wordWithoutWhitespaces).uppercase()
                _uiState.value.correctLetters.containsAll(normalizedWord.toList())
            } == true
        } else false
    }

    private fun resetGameState(
        countries: List<Country>,
        dashboardType: String,
        streakCount: Int
    ) {
        val randomCountryIndex = Random.nextInt(0, countries.size - 1)
        val randomCountry = countries[randomCountryIndex].name.common

        when (dashboardType) {
            DashboardQuizType.CAPITALS.name -> {
                _uiState.update {
                    it.copy(
                        wordRandomlyChosen = countries[randomCountryIndex].capital?.firstOrNull()
                            ?.uppercase() ?: "",
                        countryRandomChosen = randomCountry,
                        streakCount = streakCount,
                        usedLetters = emptySet(),
                        correctLetters = emptySet(),
                        wrongLetters = emptySet(),
                        livesLeft = 6,
                        question = "Capital of $randomCountry"
                    )
                }
            }

            DashboardQuizType.FLAGS.name -> {
                _uiState.update {
                    it.copy(
                        wordRandomlyChosen = countries[randomCountryIndex].name.common.uppercase(),
                        countryRandomFlag = countries[randomCountryIndex].flags?.png ?: "",
                        countryRandomChosen = randomCountry,
                        streakCount = streakCount,
                        usedLetters = emptySet(),
                        correctLetters = emptySet(),
                        wrongLetters = emptySet(),
                        livesLeft = 6,
                        tipText = "It is a country from ${countries[randomCountryIndex].region}"
                    )
                }
            }
        }
    }

    fun resetStates() {
        _currentStreakCount = increaseStreakCount()
        lettersGuessed.clear()
        correctLetters.clear()
        wrongLetters.clear()
        _currentLetterGuessed = ' '
        resetGameState(_uiState.value.countries, dashboardType, _currentStreakCount)
    }

    fun onSkipPressed() {
        lettersGuessed.clear()
        correctLetters.clear()
        wrongLetters.clear()
        _currentLetterGuessed = ' '
        onGoToNextWordPressed(_uiState.value.countries, dashboardType, _currentStreakCount)
    }

    private fun onGoToNextWordPressed(
        countries: List<Country>,
        dashboardType: String,
        streakCount: Int
    ) {
        val randomCountryIndex = Random.nextInt(0, countries.size - 1)
        val randomCountry = countries[randomCountryIndex].name.common

        when (dashboardType) {
            DashboardQuizType.CAPITALS.name -> {
                _uiState.update {
                    it.copy(
                        wordRandomlyChosen = countries[randomCountryIndex].capital?.firstOrNull()
                            ?.uppercase() ?: "",
                        countryRandomChosen = randomCountry,
                        streakCount = streakCount,
                        usedLetters = emptySet(),
                        correctLetters = emptySet(),
                        wrongLetters = emptySet(),
                        livesLeft = 6,
                        question = "Capital of $randomCountry"
                    )
                }
            }

            DashboardQuizType.FLAGS.name -> {
                _uiState.update {
                    it.copy(
                        wordRandomlyChosen = countries[randomCountryIndex].name.common.uppercase(),
                        countryRandomFlag = countries[randomCountryIndex].flags?.png ?: "",
                        countryRandomChosen = randomCountry,
                        streakCount = streakCount,
                        usedLetters = emptySet(),
                        correctLetters = emptySet(),
                        wrongLetters = emptySet(),
                        livesLeft = 6,
                        tipText = "It is a country from ${countries[randomCountryIndex].region}"
                    )
                }
            }
        }
    }

    private fun increaseStreakCount() = if (!isGameOver) ++_currentStreakCount else 0

    data class GameUiState(
        val wordRandomlyChosen: String? = "",
        val countries: List<Country> = listOf(),
        val countryRandomFlag: String = "",
        val countryRandomCoatOfArms: String = "",
        val countryRandomChosen: String = "",
        val question: String = "",
        val tipText: String = "",
        val usedLetters: Set<Char> = emptySet(),
        val correctLetters: Set<Char> = emptySet(),
        val wrongLetters: Set<Char> = emptySet(),
        val livesLeft: Int = 6,
        val isGameOver: Boolean = false,
        val streakCount: Int = 0,
        val dashboardType: String = ""
    )
}

val alphabetSet = ('A'..'Z').toSet()
