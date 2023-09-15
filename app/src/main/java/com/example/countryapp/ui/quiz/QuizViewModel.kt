package com.example.countryapp.ui.quiz

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.countryapp.R
import com.example.countryapp.ui.dashboard.DashboardQuizType
import com.example.countryapp.ui.models.Quiz
import com.example.countryapp.ui.quiz.selectregion.RegionQuizType
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
    private val dashboardType: String = checkNotNull(savedStateHandle["dashboardType"])
    private val selectedRegionType: String = checkNotNull(savedStateHandle["selectedRegionType"])
    private val quizGeneralAspectsType: String = getRandomGeneralAspectsQuestions()

    init {
        viewModelScope.launch {
            loadAllCountriesUseCase.loadAllCountries().collectLatest { countries ->
                Log.d("countries returned by server", countries.toString())
                _uiState.update {
                    it.copy(
                        quizQuestion = getQuestionByType(dashboardType),
                        questions = getQuizAnswersByType(
                            dashboardType,
                            countries,
                            selectedRegionType
                        ),
                        quizHeaderImage = getHeaderImageByType(dashboardType),
                        shouldShowImageAnswers = dashboardType == DashboardQuizType.FLAGS.name || dashboardType == DashboardQuizType.COAT_OF_ARMS.name,
                        defaultAnswerImage = getDefaultHeaderImage(dashboardType)
                    )
                }
            }
        }
    }

    private fun getDefaultHeaderImage(dashboardType: String): Int {
        return when (dashboardType) {
            DashboardQuizType.FLAGS.name -> R.drawable.ic_no_flag
            DashboardQuizType.COAT_OF_ARMS.name -> R.drawable.ic_no_coat_of_arms
            else -> 0
        }
    }

    private fun getHeaderImageByType(dashboardType: String): Int {
        return when (dashboardType) {
            DashboardQuizType.FLAGS.name -> R.drawable.image_country_flags_header
            DashboardQuizType.CAPITALS.name -> R.drawable.ic_capitals_world
            DashboardQuizType.COAT_OF_ARMS.name -> R.drawable.ic_coat_of_arms
            DashboardQuizType.GENERAL_ASPECTS.name -> R.drawable.img_general_aspects_quiz
            else -> 0
        }
    }

    private fun getQuestionByType(dashboardType: String): String {
        return when (dashboardType) {
            DashboardQuizType.FLAGS.name -> {
                "What is the flag for" //TODO: improve here to delete this mocked string
            }

            DashboardQuizType.CAPITALS.name -> {
                "What is the capital of"
            }

            DashboardQuizType.COAT_OF_ARMS.name -> {
                "What is the coat of arms of"
            }

            DashboardQuizType.GENERAL_ASPECTS.name -> {
                GeneralAspectsQuizQuestions.generalQuizQuestions[quizGeneralAspectsType] ?: ""
            }

            else -> {
                ""
            }
        }
    }

    private fun getRandomGeneralAspectsQuestions(): String {
        val randomQuestionIndex =
            Random.nextInt(0, GeneralAspectsQuizQuestions.generalQuizTypes.size - 1)
        return GeneralAspectsQuizQuestions.generalQuizTypes[randomQuestionIndex]
    }

    private fun getQuizAnswersByType(
        dashboardType: String,
        countries: List<Country>,
        selectedRegionType: String
    ): List<Quiz> {
        Log.d("Type is hello", dashboardType)
        Log.d("Type is hello", DashboardQuizType.FLAGS.name)
        return when (dashboardType) {
            DashboardQuizType.FLAGS.name -> {
                getQuizAnswersByFlags(countries, 15, selectedRegionType)
            }

            DashboardQuizType.CAPITALS.name -> {
                getQuizAnswersByCapital(countries, 15, selectedRegionType)
            }

            DashboardQuizType.COAT_OF_ARMS.name -> {
                getQuizAnswersByCoatOfArms(countries, 15, selectedRegionType)
            }

            DashboardQuizType.GENERAL_ASPECTS.name -> {
                getQuizAnswersByGeneralAspects(countries, 10)
            }

            else -> listOf()
        }
    }

    private fun getQuizAnswersByGeneralAspects(
        countries: List<Country>,
        noOfQuestions: Int
    ): List<Quiz> {
        val quizList = mutableListOf<Quiz>()
        for (i in 1..noOfQuestions) {
            val randomCountryIndex = Random.nextInt(0, countries.size - 1)
            when (quizGeneralAspectsType) {
                "region" -> {
                    val quiz = Quiz(
                        countryName = countries[randomCountryIndex].name.common,
                        correctAnswer = countries[randomCountryIndex].region ?: "Does not have",
                        answers = listOf(
                            countries[randomCountryIndex].region ?: "",
                            countries[Random.nextInt(0, countries.size - 1)].region
                                ?: "Does not have",
                            countries[Random.nextInt(0, countries.size - 1)].region
                                ?: "Does not have",
                            countries[Random.nextInt(0, countries.size - 1)].region
                                ?: "Does not have",
                        ).shuffled().distinct()
                    )
                    quizList.add(quiz)
                }

                "subregion" -> {
                    val quiz = Quiz(
                        countryName = countries[randomCountryIndex].name.common,
                        correctAnswer = countries[randomCountryIndex].subregion ?: "Does not have",
                        answers = listOf(
                            countries[randomCountryIndex].subregion ?: "",
                            countries[Random.nextInt(0, countries.size - 1)].subregion
                                ?: "Does not have",
                            countries[Random.nextInt(0, countries.size - 1)].subregion
                                ?: "Does not have",
                            countries[Random.nextInt(0, countries.size - 1)].subregion
                                ?: "Does not have",
                        ).shuffled().distinct()
                    )
                    quizList.add(quiz)
                }

                "population" -> {
                    val quiz = Quiz(
                        countryName = countries[randomCountryIndex].name.common,
                        correctAnswer = countries[randomCountryIndex].population ?: "Does not have",
                        answers = listOf(
                            countries[randomCountryIndex].population ?: "",
                            countries[Random.nextInt(0, countries.size - 1)].population
                                ?: "Does not have",
                            countries[Random.nextInt(0, countries.size - 1)].population
                                ?: "Does not have",
                            countries[Random.nextInt(0, countries.size - 1)].population
                                ?: "Does not have",
                        ).shuffled().distinct()
                    )
                    quizList.add(quiz)
                }

                "independent" -> {
                    val quiz = Quiz(
                        countryName = countries[randomCountryIndex].name.common,
                        correctAnswer = countries[randomCountryIndex].population ?: "Does not have",
                        answers = listOf(
                            countries[randomCountryIndex].population ?: "",
                            countries[Random.nextInt(0, countries.size - 1)].population
                                ?: "Does not have",
                            countries[Random.nextInt(0, countries.size - 1)].population
                                ?: "Does not have",
                            countries[Random.nextInt(0, countries.size - 1)].population
                                ?: "Does not have",
                        ).shuffled().distinct()
                    )
                    quizList.add(quiz)
                }


                else -> {}
            }
        }
        return quizList
    }


    private fun getQuizAnswersByCoatOfArms(
        countries: List<Country>,
        noOfQuestions: Int,
        selectedRegionType: String
    ): List<Quiz> {
        val quizList = mutableListOf<Quiz>()
        val countryList = getCountryListFilteredByRegion(countries, selectedRegionType)

        for (i in 1..noOfQuestions) {
            val randomCountryIndex = Random.nextInt(0, countryList.size - 1)
            val quiz = Quiz(
                countryName = countryList[randomCountryIndex].name.common,
                correctAnswer = countryList[randomCountryIndex].coatOfArms?.png ?: "Does not have",
                answers = listOf(
                    countryList[randomCountryIndex].coatOfArms?.png ?: "",
                    countryList[Random.nextInt(0, countryList.size - 1)].coatOfArms?.png
                        ?: "Does not have",
                    countryList[Random.nextInt(0, countryList.size - 1)].coatOfArms?.png
                        ?: "Does not have",
                    countryList[Random.nextInt(0, countryList.size - 1)].coatOfArms?.png
                        ?: "Does not have",
                ).shuffled().distinct()
            )
            quizList.add(quiz)
        }
        return quizList
    }

    private fun getQuizAnswersByCapital(
        countries: List<Country>,
        noOfQuestions: Int,
        selectedRegionType: String
    ): List<Quiz> {
        val quizList = mutableListOf<Quiz>()
        val countryList = getCountryListFilteredByRegion(countries, selectedRegionType)

        for (i in 1..noOfQuestions) {
            val randomCountryIndex = Random.nextInt(0, countryList.size - 1)
            val quiz = Quiz(
                countryName = countryList[randomCountryIndex].name.common,
                correctAnswer = countryList[randomCountryIndex].capital?.firstOrNull()
                    ?: "Does not have",
                answers = listOf(
                    countryList[randomCountryIndex].capital?.firstOrNull() ?: "Does not have",
                    countryList[Random.nextInt(0, countryList.size - 1)].capital?.firstOrNull()
                        ?: "Does not have",
                    countryList[Random.nextInt(0, countryList.size - 1)].capital?.firstOrNull()
                        ?: "Does not have",
                    countryList[Random.nextInt(0, countryList.size - 1)].capital?.firstOrNull()
                        ?: "Does not have"
                ).shuffled().distinct()
            )
            quizList.add(quiz)
        }

        return quizList
    }


    private fun getQuizAnswersByFlags(
        countries: List<Country>,
        noOfQuestions: Int,
        selectedRegionType: String
    ): List<Quiz> {
        val quizList = mutableListOf<Quiz>()
        val countryList = getCountryListFilteredByRegion(countries, selectedRegionType)

        for (i in 1..noOfQuestions) {
            val randomCountryIndex = Random.nextInt(0, countryList.size - 1)
            val quiz = Quiz(
                countryName = countryList[randomCountryIndex].name.common,
                correctAnswer = countryList[randomCountryIndex].flags?.png ?: "Does not have",
                answers = listOf(
                    countryList[randomCountryIndex].flags?.png ?: "",
                    countryList[Random.nextInt(0, countryList.size - 1)].flags?.png
                        ?: "Does not have",
                    countryList[Random.nextInt(0, countryList.size - 1)].flags?.png
                        ?: "Does not have",
                    countryList[Random.nextInt(0, countryList.size - 1)].flags?.png
                        ?: "Does not have",
                ).shuffled().distinct()
            )
            quizList.add(quiz)
        }
        return quizList
    }

    private fun getCountryListFilteredByRegion(
        countries: List<Country>,
        regionType: String
    ): List<Country> {
        return if (regionType == RegionQuizType.ALL.name) {
            countries
        } else {
            countries.filter { elem -> elem.region?.uppercase() == regionType.uppercase() }
        }
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
        val defaultAnswerImage: Int = 0,
    )
}