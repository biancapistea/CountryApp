package com.example.countryapp.ui.game.hangman

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.countryapp.ui.dashboard.DashboardQuizType
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
class HangmanGameViewModel @Inject constructor(
    loadAllCountriesUseCase: LoadAllCountriesUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _uiState = MutableStateFlow(GameUiState())
    val uiState = _uiState.asStateFlow()

    private val dashboardType: String = checkNotNull(savedStateHandle["dashboardType"])
    private var lettersGuessed: MutableSet<Char> = mutableSetOf()
    private var correctLetters: MutableSet<Char> = mutableSetOf()
    private var wrongLetters: MutableSet<Char> = mutableSetOf()

    private var _currentLetterGuessed: Char = ' '

    private var _currentStreakCount: Int = 0

    init {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            loadAllCountriesUseCase.loadAllCountries().collectLatest { countries ->
                _uiState.update {
                    it.copy(
                        countries = countries,
                        wordRandomlyChosen = getRandomWordByType(countries, dashboardType),
                        question = getQuestionByType(dashboardType),
                        dashboardType = dashboardType,
                        isLoading = false
                    )
                }
            }
        }
    }

    fun isGameOver() = _uiState.value.livesLeft == 0

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
        return when (dashboardType) {
            DashboardQuizType.CAPITALS.name -> {
                val countriesWithCapitals = countries.filter { it.capital?.isNotEmpty() == true }
                val randomCountryIndex = Random.nextInt(0, countriesWithCapitals.size - 1)
                val randomCountry = countriesWithCapitals[randomCountryIndex].name.common
                _uiState.update {
                    it.copy(
                        countryRandomChosen = randomCountry,
                        tipText = "It starts with the letter ${
                            countriesWithCapitals[randomCountryIndex].capital?.first()?.first()
                        }"
                    )
                }
                countriesWithCapitals[randomCountryIndex].capital?.firstOrNull()?.uppercase() ?: ""
            }

            DashboardQuizType.FLAGS.name -> {
                val countriesWithFlags = countries.filter { it.flags?.png?.isNotEmpty() == true }
                val randomCountryIndex = Random.nextInt(0, countriesWithFlags.size - 1)
                val randomCountry = countriesWithFlags[randomCountryIndex].name.common
                _uiState.update {
                    it.copy(
                        countryRandomFlag = countriesWithFlags[randomCountryIndex].flags?.png ?: "",
                        countryRandomChosen = randomCountry,
                        tipText = "It is a country from ${countriesWithFlags[randomCountryIndex].region}"
                    )
                }
                countriesWithFlags[randomCountryIndex].name.common.uppercase()
            }

            DashboardQuizType.COAT_OF_ARMS.name -> {
                val countriesWithCoatOfArms =
                    countries.filter { it.coatOfArms?.png?.isNotEmpty() == true }
                val randomCountryIndex = Random.nextInt(0, countriesWithCoatOfArms.size - 1)
                val randomCountry = countriesWithCoatOfArms[randomCountryIndex].name.common
                _uiState.update {
                    it.copy(
                        countryRandomFlag = countriesWithCoatOfArms[randomCountryIndex].coatOfArms?.png
                            ?: "",
                        countryRandomChosen = randomCountry,
                        tipText = "It is a country from ${countriesWithCoatOfArms[randomCountryIndex].region}"
                    )
                }
                countriesWithCoatOfArms[randomCountryIndex].name.common.uppercase()
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
        when (dashboardType) {
            DashboardQuizType.CAPITALS.name -> {
                val countriesWithCapitals = countries.filter { it.capital?.isNotEmpty() == true }
                val randomCountryIndex = Random.nextInt(0, countriesWithCapitals.size - 1)
                val randomCountry = countriesWithCapitals[randomCountryIndex].name.common
                _uiState.update {
                    it.copy(
                        wordRandomlyChosen = countriesWithCapitals[randomCountryIndex].capital?.firstOrNull()
                            ?.uppercase() ?: "",
                        countryRandomChosen = randomCountry,
                        streakCount = streakCount,
                        usedLetters = emptySet(),
                        correctLetters = emptySet(),
                        wrongLetters = emptySet(),
                        livesLeft = 6,
                        question = "Capital of $randomCountry",
                        tipText = "It starts with the letter ${
                            countriesWithCapitals[randomCountryIndex].capital?.first()?.first()
                        }"
                    )
                }
            }

            DashboardQuizType.FLAGS.name -> {
                val countriesWithFlags = countries.filter { it.flags?.png?.isNotEmpty() == true }
                val randomCountryIndex = Random.nextInt(0, countriesWithFlags.size - 1)
                val randomCountry = countriesWithFlags[randomCountryIndex].name.common
                _uiState.update {
                    it.copy(
                        wordRandomlyChosen = countriesWithFlags[randomCountryIndex].name.common.uppercase(),
                        countryRandomFlag = countriesWithFlags[randomCountryIndex].flags?.png ?: "",
                        countryRandomChosen = randomCountry,
                        streakCount = streakCount,
                        usedLetters = emptySet(),
                        correctLetters = emptySet(),
                        wrongLetters = emptySet(),
                        livesLeft = 6,
                        tipText = "It is a country from ${countriesWithFlags[randomCountryIndex].region}"
                    )
                }
            }

            DashboardQuizType.COAT_OF_ARMS.name -> {
                val countriesWithCoatOfArms =
                    countries.filter { it.coatOfArms?.png?.isNotEmpty() == true }
                val randomCountryIndex = Random.nextInt(0, countriesWithCoatOfArms.size - 1)
                val randomCountry = countriesWithCoatOfArms[randomCountryIndex].name.common
                _uiState.update {
                    it.copy(
                        wordRandomlyChosen = countriesWithCoatOfArms[randomCountryIndex].name.common.uppercase(),
                        countryRandomFlag = countriesWithCoatOfArms[randomCountryIndex].coatOfArms?.png
                            ?: "",
                        countryRandomChosen = randomCountry,
                        streakCount = streakCount,
                        usedLetters = emptySet(),
                        correctLetters = emptySet(),
                        wrongLetters = emptySet(),
                        livesLeft = 6,
                        tipText = "It is a country from ${countriesWithCoatOfArms[randomCountryIndex].region}"
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
        when (dashboardType) {
            DashboardQuizType.CAPITALS.name -> {
                val countriesWithCapitals = countries.filter { it.capital?.isNotEmpty() == true }
                val randomCountryIndex = Random.nextInt(0, countriesWithCapitals.size - 1)
                val randomCountry = countriesWithCapitals[randomCountryIndex].name.common
                _uiState.update {
                    it.copy(
                        wordRandomlyChosen = countriesWithCapitals[randomCountryIndex].capital?.firstOrNull()
                            ?.uppercase() ?: "",
                        countryRandomChosen = randomCountry,
                        streakCount = streakCount,
                        usedLetters = emptySet(),
                        correctLetters = emptySet(),
                        wrongLetters = emptySet(),
                        livesLeft = uiState.value.livesLeft,
                        question = "Capital of $randomCountry",
                        tipText = "It starts with the letter ${
                            countriesWithCapitals[randomCountryIndex].capital?.first()?.first()
                        }"
                    )
                }
            }

            DashboardQuizType.FLAGS.name -> {
                val countriesWithFlags = countries.filter { it.flags?.png?.isNotEmpty() == true }
                val randomCountryIndex = Random.nextInt(0, countriesWithFlags.size - 1)
                val randomCountry = countriesWithFlags[randomCountryIndex].name.common
                _uiState.update {
                    it.copy(
                        wordRandomlyChosen = countriesWithFlags[randomCountryIndex].name.common.uppercase(),
                        countryRandomFlag = countriesWithFlags[randomCountryIndex].flags?.png ?: "",
                        countryRandomChosen = randomCountry,
                        streakCount = streakCount,
                        usedLetters = emptySet(),
                        correctLetters = emptySet(),
                        wrongLetters = emptySet(),
                        livesLeft = uiState.value.livesLeft,
                        tipText = "It is a country from ${countriesWithFlags[randomCountryIndex].region}"
                    )
                }
            }

            DashboardQuizType.COAT_OF_ARMS.name -> {
                val countriesWithCoatOfArms =
                    countries.filter { it.coatOfArms?.png?.isNotEmpty() == true }
                val randomCountryIndex = Random.nextInt(0, countriesWithCoatOfArms.size - 1)
                val randomCountry = countriesWithCoatOfArms[randomCountryIndex].name.common
                _uiState.update {
                    it.copy(
                        wordRandomlyChosen = countriesWithCoatOfArms[randomCountryIndex].name.common.uppercase(),
                        countryRandomChosen = randomCountry,
                        countryRandomFlag = countriesWithCoatOfArms[randomCountryIndex].coatOfArms?.png
                            ?: "",
                        streakCount = streakCount,
                        usedLetters = emptySet(),
                        correctLetters = emptySet(),
                        wrongLetters = emptySet(),
                        livesLeft = uiState.value.livesLeft,
                        tipText = "It is a country from ${countriesWithCoatOfArms[randomCountryIndex].region}"
                    )
                }
            }
        }
    }

    private fun increaseStreakCount() = if (!isGameOver()) ++_currentStreakCount else 0

    data class GameUiState(
        val isLoading: Boolean = false,
        val wordRandomlyChosen: String? = "",
        val countries: List<Country> = listOf(),
        val countryRandomFlag: String = "",
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