package com.example.countryapp.ui.quiz

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.countryapp.R
import com.example.countryapp.ui.dashboard.DashboardQuizType
import com.example.countryapp.ui.models.Quiz
import com.example.domain.model.Country
import com.example.domain.usecase.LoadAllCountriesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class QuizViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    loadAllCountriesUseCase: LoadAllCountriesUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()
    private val type: String = checkNotNull(savedStateHandle["type"])

    init {
        viewModelScope.launch {
            loadAllCountriesUseCase.loadAllCountries().collectLatest { countries ->
                Log.d("countries returned by server", countries.toString())
                _uiState.update {
                    it.copy(
                        questions = getQuizByType(type, countries),
                        quizQuestion = getQuestionByType(type),
                        quizHeaderImage = getHeaderImageByType(type),
                        shouldShowImageAnswers = type == DashboardQuizType.FLAGS.name || type == DashboardQuizType.COAT_OF_ARMS.name,
                        defaultAnswerImage = getDefaultHeaderImage(type)
                    )
                }
            }
        }
    }

    private fun getDefaultHeaderImage(type: String): Int {
        return when (type) {
            DashboardQuizType.FLAGS.name -> R.drawable.ic_no_flag
            DashboardQuizType.COAT_OF_ARMS.name -> R.drawable.ic_no_coat_of_arms
            else -> 0
        }
    }

    private fun getHeaderImageByType(type: String): Int {
        return when (type) {
            DashboardQuizType.FLAGS.name -> R.drawable.image_country_flags_header
            DashboardQuizType.CAPITALS.name -> R.drawable.ic_capitals_world
            DashboardQuizType.COAT_OF_ARMS.name -> R.drawable.ic_coat_of_arms
            else -> 0
        }
    }

    private fun getQuestionByType(type: String): String {
        return when (type) {
            DashboardQuizType.FLAGS.name -> {
                "What is the flag for" //TODO: improve here to delete this mocked string
            }

            DashboardQuizType.CAPITALS.name -> {
                "What is the capital of"
            }

            DashboardQuizType.COAT_OF_ARMS.name -> {
                "What is the coat of arms of"
            }

            else -> {
                ""
            }
        }
    }

    private fun getQuizByType(type: String, countries: List<Country>): List<Quiz> {
        Log.d("Type is hello", type)
        Log.d("Type is hello", DashboardQuizType.FLAGS.name)
        return when (type) {
            DashboardQuizType.FLAGS.name -> {
                getQuizQuestionsByFlags(countries, 10)
            }

            DashboardQuizType.CAPITALS.name -> {
                getQuizQuestionsByCapital(countries, 8)
            }

            DashboardQuizType.COAT_OF_ARMS.name -> {
                getQuizQuestionsByCoatOfArms(countries, 10)
            }

            else -> listOf()
        }
    }

    private fun getQuizQuestionsByCoatOfArms(
        countries: List<Country>,
        noOfQuestions: Int
    ): List<Quiz> {
        val quizList = mutableListOf<Quiz>()

        for (i in 1..noOfQuestions) {
            val randomCountryIndex = Random.nextInt(0, countries.size - 1)
            val quiz = Quiz(
                countryName = countries[randomCountryIndex].name.common,
                correctAnswer = countries[randomCountryIndex].coatOfArms?.png ?: "Does not have" ,
                answers = listOf(
                    countries[randomCountryIndex].coatOfArms?.png ?: "",
                    countries[Random.nextInt(0, countries.size - 1)].coatOfArms?.png
                        ?: "Does not have",
                    countries[Random.nextInt(0, countries.size - 1)].coatOfArms?.png
                        ?: "Does not have",
                    countries[Random.nextInt(0, countries.size - 1)].coatOfArms?.png
                        ?: "Does not have",
                ).shuffled().distinct()
            )
            quizList.add(quiz)
        }
        return quizList
    }

    private fun getQuizQuestionsByCapital(
        countries: List<Country>,
        noOfQuestions: Int
    ): List<Quiz> {
        val quizList = mutableListOf<Quiz>()

        for (i in 1..noOfQuestions) {
            val randomCountryIndex = Random.nextInt(0, countries.size - 1)
            val quiz = Quiz(
                countryName = countries[randomCountryIndex].name.common,
                correctAnswer = countries[randomCountryIndex].capital?.firstOrNull()
                    ?: "Does not have",
                answers = listOf(
                    countries[randomCountryIndex].capital?.firstOrNull() ?: "Does not have",
                    countries[Random.nextInt(0, countries.size - 1)].capital?.firstOrNull()
                        ?: "Does not have",
                    countries[Random.nextInt(0, countries.size - 1)].capital?.firstOrNull()
                        ?: "Does not have",
                    countries[Random.nextInt(0, countries.size - 1)].capital?.firstOrNull()
                        ?: "Does not have"
                ).shuffled().distinct()
            )
            quizList.add(quiz)
        }

        return quizList
    }


    private fun getQuizQuestionsByFlags(countries: List<Country>, noOfQuestions: Int): List<Quiz> {
        val quizList = mutableListOf<Quiz>()

        for (i in 1..noOfQuestions) {
            val randomCountryIndex = Random.nextInt(0, countries.size - 1)
            val quiz = Quiz(
                countryName = countries[randomCountryIndex].name.common,
                correctAnswer = countries[randomCountryIndex].flags?.png ?: "Does not have",
                answers = listOf(
                    countries[randomCountryIndex].flags?.png ?: "",
                    countries[Random.nextInt(0, countries.size - 1)].flags?.png ?: "Does not have",
                    countries[Random.nextInt(0, countries.size - 1)].flags?.png ?: "Does not have",
                    countries[Random.nextInt(0, countries.size - 1)].flags?.png ?: "Does not have",
                ).shuffled().distinct()
            )
            quizList.add(quiz)
        }
        return quizList
    }

    fun updateRestartQuiz() {
        _uiState.update { it.copy(restartQuiz = false) }
    }

    fun setSelectedValue(value: String) {
        _uiState.update { it.copy(selectedValue = value) }
    }

    fun clearQuestion() {
        _uiState.update {
            it.copy(
                selectedValue = "",
                isCorrect = null
            )
        }
    }

    fun checkAnswer(
        position: Int,
        onGoToDashboardPressed: () -> Unit,
        onCorrectAnswerAction: () -> Unit
    ) {
        val selectedValue = _uiState.value.selectedValue
        val correctAnswer = _uiState.value.questions[position].correctAnswer

        if (correctAnswer.isNotEmpty()) {
            if (correctAnswer == selectedValue) {
                val correctAnswers = _uiState.value.numberCorrectQuestions + 1
                _uiState.update {
                    it.copy(
                        numberCorrectQuestions = correctAnswers,
                        isCorrect = true
                    )
                }
            } else {
                _uiState.update {
                    it.copy(
                        isCorrect = false
                    )
                }
            }
            onCorrectAnswerAction()
        }

        if (position + 1 == _uiState.value.questions.size) {
            _uiState.update {
                it.copy(
                    isQuizFinalized = true
                )
            }
        }
    }

    fun restartQuizPressed() {
        _uiState.update {
            it.copy(
                restartQuiz = true,
                numberCorrectQuestions = 0,
                selectedValue = "",
                isCorrect = null,
                isQuizFinalized = false
            )
        }
    }


    data class UiState(
        val isQuizFinalized: Boolean = false,
        val questions: List<Quiz> = listOf(),
        val quizQuestion: String = "",
        val quizHeaderImage: Int = 0,
        val selectedValue: String = "",
        val numberCorrectQuestions: Int = 0,
        val isCorrect: Boolean? = null,
        val restartQuiz: Boolean = false,
        val shouldShowImageAnswers: Boolean = false,
        val defaultAnswerImage: Int = 0
    )
}