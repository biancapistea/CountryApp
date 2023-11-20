package com.example.countryapp.ui.game.hangman

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.countryapp.ui.dashboard.DashboardQuizType
import com.example.countryapp.ui.utils.StringUtil
import com.example.domain.model.Country
import com.example.domain.model.Resource
import com.example.domain.model.WordStatus
import com.example.domain.usecase.GetCurrentWordStatusUseCase
import com.example.domain.usecase.GetQuestionStatusUseCase
import com.example.domain.usecase.LoadAllCountriesUseCase
import com.example.domain.usecase.SaveCurrentWordStatusUseCase
import com.example.domain.usecase.SaveQuestionStatusUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.Collator
import javax.inject.Inject

@HiltViewModel
class HangmanWithLevelsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    loadAllCountriesUseCase: LoadAllCountriesUseCase,
    private val saveQuestionStatusUseCase: SaveQuestionStatusUseCase,
    private val getQuestionStatusUseCase: GetQuestionStatusUseCase,
    private val getCurrentWordStatusUseCase: GetCurrentWordStatusUseCase,
    private val saveCurrentWordStatusUseCase: SaveCurrentWordStatusUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()
    private val dashboardType: String = checkNotNull(savedStateHandle["dashboardType"])
    private val currentLevel: String = checkNotNull(savedStateHandle["currentLevel"])

    init {
        viewModelScope.launch {
            loadAllCountriesUseCase.loadAllCountries().collectLatest { status ->
                when (status) {
                    is Resource.Success -> {
                        val formattedCountries =
                            getQuestionsByLevel(currentLevel.toInt(), status.data ?: emptyList())
                        val wordStatus = getCurrentWordState(formattedCountries[0].name.common)
                        _uiState.update {
                            it.copy(
                                countries = formattedCountries,
                                isLoading = false,
                                correctAnswer = getCorrectAnswer(
                                    formattedCountries
                                ),
                                countryFlag = getCountryFlag(formattedCountries[0]),
                                question = getQuestionByType(formattedCountries[0]),
                                currentLevel = currentLevel.toInt(),
                                currentIndexOfQuestion = 0,
                                usedLetters = wordStatus.usedLetters,
                                correctLetters = wordStatus.correctLetters,
                                wrongLetters = wordStatus.wrongLetters,
                            )
                        }
                    }

                    is Resource.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }

                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(
                                errorMessage = status.message ?: "An unexpected error occured"
                            )
                        }
                    }
                }
            }
        }
    }

    private fun saveCurrentWordState() {
        val countryName = _uiState.value.countries[_uiState.value.currentIndexOfQuestion].name.common

        saveCurrentWordStatusUseCase.saveCurrentStatusUseCase(
            WordStatus(
                correctLetters = _uiState.value.correctLetters,
                usedLetters = _uiState.value.usedLetters,
                wrongLetters = _uiState.value.wrongLetters
            ),
            countryName
        )
    }

    private fun getCurrentWordState(countryName: String): WordStatus =
        getCurrentWordStatusUseCase.getCurrentWordStatusUseCase(countryName)

    private fun saveQuestionStatus(status: QuestionStatus) {
        saveQuestionStatusUseCase.saveQuestionStatus(
            status.name,
            _uiState.value.countries[_uiState.value.currentIndexOfQuestion].name.common
        )
    }

    private fun getQuestionStatus(): String {
        return getQuestionStatusUseCase.getQuestionStatus(_uiState.value.countries[_uiState.value.currentIndexOfQuestion].name.common)
    }

    private fun getCountryFlag(country: Country): String =
        country.flags?.png ?: ""

    private fun getCorrectAnswer(
        countries: List<Country>,
        currentIndexOfQuestion: Int? = null
    ): String {
        return when (dashboardType) {
            DashboardQuizType.CAPITALS.name -> {
                val countriesWithCapitals =
                    countries.filter { it.capital?.isNotEmpty() == true }
                val currentIndex =
                    currentIndexOfQuestion ?: _uiState.value.currentIndexOfQuestion
                countriesWithCapitals[currentIndex].capital?.firstOrNull()?.uppercase() ?: ""
            }

            DashboardQuizType.FLAGS.name -> {
                val countriesWithFlags =
                    countries.filter { it.flags?.png?.isNotEmpty() == true }
                val currentIndex =
                    currentIndexOfQuestion ?: _uiState.value.currentIndexOfQuestion
                countriesWithFlags[currentIndex].name.common.uppercase()
            }

//            DashboardQuizType.COAT_OF_ARMS.name -> {
//                val countriesWithCoatOfArms =
//                    countries.filter { it.coatOfArms?.png?.isNotEmpty() == true }
//                val randomCountryIndex = Random.nextInt(0, countriesWithCoatOfArms.size - 1)
//                val randomCountry = countriesWithCoatOfArms[randomCountryIndex].name.common
//
//                countriesWithCoatOfArms[randomCountryIndex].name.common.uppercase()
//            }

            else -> {
                ""
            }
        }
    }

    fun isGameOver() = _uiState.value.livesLeft == 0

    private fun getQuestionByType(country: Country): String {
        return when (dashboardType) {
            DashboardQuizType.FLAGS.name -> {
                "What country's flag is showed below?"
            }

            DashboardQuizType.CAPITALS.name -> {
                "Capital of ${country.name.common}"
            }

            DashboardQuizType.COAT_OF_ARMS.name -> {
                "What country's coat of arms is showed below?"
            }

            else -> {
                ""
            }
        }
    }

    private fun isLetterGuessCorrect(letterFromButton: Char): Boolean? {
        val collator: Collator = Collator.getInstance()
        collator.strength = Collator.PRIMARY

        val correctAnswer = _uiState.value.correctAnswer
        val normalizedLetterFromButton = letterFromButton.toString()

        return correctAnswer?.any { char ->
            collator.compare(char.toString(), normalizedLetterFromButton) == 0
        }
    }

    private fun getQuestionsByLevel(
        currentLevel: Int,
        countries: List<Country>
    ): List<Country> {
        return countries.slice(currentLevel * 10..currentLevel * 10 + 9)
    }

    fun goToTheNextQuestion() {
        saveCurrentWordState()
        if (_uiState.value.currentIndexOfQuestion < _uiState.value.countries.size - 1) {
            val nextIndexOfQuestion = _uiState.value.currentIndexOfQuestion + 1
            val nextCountry = _uiState.value.countries[nextIndexOfQuestion]
            val wordStatus = getCurrentWordState(nextCountry.name.common)
            _uiState.update {
                it.copy(
                    currentIndexOfQuestion = nextIndexOfQuestion,
                    countryFlag = getCountryFlag(nextCountry),
                    correctAnswer = getCorrectAnswer(
                        _uiState.value.countries,
                        nextIndexOfQuestion,
                    ),
                    question = getQuestionByType(nextCountry),
                    usedLetters = wordStatus.usedLetters,
                    correctLetters = wordStatus.correctLetters,
                    wrongLetters = wordStatus.wrongLetters,
                )
            }
        } else {
            val lastCountry = _uiState.value.countries[0]
            val wordStatus = getCurrentWordState(lastCountry.name.common)
            _uiState.update {
                it.copy(
                    currentIndexOfQuestion = 0,
                    correctAnswer = getCorrectAnswer(_uiState.value.countries, 0),
                    countryFlag = getCountryFlag(_uiState.value.countries[0]),
                    question = getQuestionByType(_uiState.value.countries[0]),
                    usedLetters = wordStatus.usedLetters,
                    correctLetters = wordStatus.correctLetters,
                    wrongLetters = wordStatus.wrongLetters,
                )
            }
        }
    }

    fun goToPreviousQuestion() {
        saveCurrentWordState()
        if (_uiState.value.currentIndexOfQuestion > 0) {
            val previousIndexOfQuestion = _uiState.value.currentIndexOfQuestion - 1
            val previousCountry = _uiState.value.countries[previousIndexOfQuestion]
            val wordStatus = getCurrentWordState(previousCountry.name.common)
            _uiState.update {
                it.copy(
                    currentIndexOfQuestion = previousIndexOfQuestion,
                    countryFlag = getCountryFlag(previousCountry),
                    correctAnswer = getCorrectAnswer(
                        _uiState.value.countries,
                        previousIndexOfQuestion
                    ),
                    question = getQuestionByType(previousCountry),
                    usedLetters = wordStatus.usedLetters,
                    correctLetters = wordStatus.correctLetters,
                    wrongLetters = wordStatus.wrongLetters,
                )
            }
        } else {
            val lastCountry = _uiState.value.countries[_uiState.value.countries.lastIndex]
            val wordStatus = getCurrentWordState(lastCountry.name.common)
            _uiState.update {
                it.copy(
                    currentIndexOfQuestion = _uiState.value.countries.lastIndex,
                    correctAnswer = getCorrectAnswer(
                        _uiState.value.countries,
                        _uiState.value.countries.lastIndex
                    ),
                    countryFlag = getCountryFlag(lastCountry),
                    question = getQuestionByType(lastCountry),
                    usedLetters = wordStatus.usedLetters,
                    correctLetters = wordStatus.correctLetters,
                    wrongLetters = wordStatus.wrongLetters,
                )
            }
        }
    }

    fun isWordCorrectlyGuessed(): Boolean {
        return if (_uiState.value.correctAnswer != "") {
            _uiState.value.correctAnswer?.run {
                val wordWithoutWhitespaces =
                    StringUtil.removeWhitespacesAndHyphens(this).uppercase()
                val normalizedWord =
                    StringUtil.normalizeWord(wordWithoutWhitespaces).uppercase()
                _uiState.value.correctLetters.containsAll(normalizedWord.toList())
            } == true
        } else {
            false
        }
    }

    fun resetStates() {}

    fun saveCurrentQuestionStatus() {
        if (isWordCorrectlyGuessed()) {
            saveQuestionStatus(QuestionStatus.COMPLETED)
        } else {
            saveQuestionStatus(QuestionStatus.WRONG)
        }
    }

    fun checkUserGuess(letterFromButton: Char) {
        val lettersGuessed = _uiState.value.usedLetters.toMutableSet()
        lettersGuessed.add(letterFromButton)
        val currentLetterGuessed = letterFromButton.uppercaseChar()

        if (isLetterGuessCorrect(letterFromButton) == true) {
            val correctLetters = _uiState.value.correctLetters.toMutableSet()
            correctLetters.add(currentLetterGuessed)
            _uiState.update { currentState ->
                currentState.copy(
                    usedLetters = lettersGuessed.toSet(),
                    correctLetters = correctLetters.toSet(),
                )
            }
        } else {
            val wrongLetters = _uiState.value.wrongLetters.toMutableSet()
            wrongLetters.add(currentLetterGuessed)
            _uiState.update { currentState ->
                currentState.copy(
                    usedLetters = lettersGuessed.toSet(),
                    wrongLetters = wrongLetters.toSet(),
                    livesLeft = currentState.livesLeft.dec(),
                )
            }
        }
    }


    data class UiState(
        val currentIndexOfQuestion: Int = 0,
        val totalQuestions: Int = 10,
        val countries: List<Country> = emptyList(),
        val question: String = "",
        val livesLeft: Int = 6,
        val correctAnswer: String? = "",
        val countryFlag: String = "",
        val correctLetters: Set<Char> = emptySet(),
        val usedLetters: Set<Char> = emptySet(),
        val wrongLetters: Set<Char> = emptySet(),
        val isLoading: Boolean = false,
        val currentLevel: Int = 0,
        val errorMessage: String = "",
        val status: String = QuestionStatus.WRONG.name
    )
}