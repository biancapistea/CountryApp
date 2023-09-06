package com.example.countryapp.ui.quiz.regionquiz

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class RegionQuizViewModel @Inject constructor(savedStateHandle: SavedStateHandle) : ViewModel() {
    private val type: String = checkNotNull(savedStateHandle["type"])

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    data class UiState(val list: List<String> = listOf())

}