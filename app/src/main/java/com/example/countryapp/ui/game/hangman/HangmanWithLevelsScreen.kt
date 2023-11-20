package com.example.countryapp.ui.game.hangman

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.countryapp.R
import com.example.countryapp.ui.components.animation.HeartAnimation
import com.example.countryapp.ui.components.text.ParagraphTextComponent
import com.example.countryapp.ui.components.text.TitleText

@Composable
fun HangmanWithLevelsScreen(
    viewModel: HangmanWithLevelsViewModel,
    onExitPressed: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        if (viewModel.isWordCorrectlyGuessed()) {
//            viewModel.resetStates()
            viewModel.saveCurrentQuestionStatus()
        }
    }

    HangmanContent(
        uiState = uiState,
        onExitPressed,
        checkUserGuess = viewModel::checkUserGuess,
        onGoToTheNextQuestion = viewModel::goToTheNextQuestion,
        onGoToPreviousQuestion = viewModel::goToPreviousQuestion
    )
}

@Composable
fun HangmanContent(
    uiState: HangmanWithLevelsViewModel.UiState,
    onExitPressed: () -> Unit,
    checkUserGuess: (Char) -> Unit,
    onGoToTheNextQuestion: () -> Unit,
    onGoToPreviousQuestion: () -> Unit,
) {
    Column(
        modifier = Modifier
            .background(Color.White)
            .padding(horizontal = 16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TitleText(text = uiState.question, modifier = Modifier.padding(12.dp))
        Row {
            Image(painterResource(id = R.drawable.ic_arrow_left),
                contentDescription = "go to previous question",
                modifier = Modifier
                    .size(42.dp)
                    .clickable { onGoToPreviousQuestion() })
            AsyncImage(
                modifier = Modifier
                    .size(120.dp)
                    .weight(1.0f),
                contentScale = ContentScale.Fit,
                model = uiState.countryFlag,
                contentDescription = null
            )
            Image(painterResource(id = R.drawable.ic_arrow_right),
                contentDescription = "go to next question",
                modifier = Modifier
                    .size(42.dp)
                    .clickable { onGoToTheNextQuestion() })
        }
        HeartAnimation(
            uiState.livesLeft
        )
        Column(modifier = Modifier.fillMaxWidth()) {
            ChosenWordRow(
                uiState.correctAnswer,
                uiState.correctLetters
            )
            KeyboardLayout(
                alphabetList = alphabetSet.toList(),
                checkUserGuess = { checkUserGuess(it) },
                correctLetters = uiState.correctLetters,
                usedLetters = uiState.usedLetters,
            )
        }
        ParagraphTextComponent(
            modifier = Modifier
                .wrapContentSize()
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) { onExitPressed() },
            text = stringResource(R.string.exit_game),
            color = Color.Blue,
            paddingValues = PaddingValues(top = 40.dp, bottom = 24.dp)
        )
    }
}