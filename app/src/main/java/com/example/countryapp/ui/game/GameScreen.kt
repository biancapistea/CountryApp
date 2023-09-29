package com.example.countryapp.ui.game

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import coil.compose.AsyncImage
import com.example.countryapp.R
import com.example.countryapp.ui.components.animation.AnimatedText
import com.example.countryapp.ui.components.animation.HeartAnimation
import com.example.countryapp.ui.components.buttons.WordLetter
import com.example.countryapp.ui.components.dialog.GameOverDialog
import com.example.countryapp.ui.components.keyboard.KeyboardKey
import com.example.countryapp.ui.components.text.TitleText
import com.example.countryapp.ui.dashboard.DashboardQuizType

@Composable
fun GameScreen(
    gameViewModel: GameViewModel,
    onPopBack: () -> Boolean
) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val uiState by produceState(
        initialValue = GameViewModel.GameUiState()
    ) {
        lifecycle.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
            gameViewModel.uiState.collect { value = it }
        }
    }

    if (gameViewModel.isWordCorrectlyGuessed()) {
        gameViewModel.resetStates()
    }

    if (gameViewModel.isGameOver()) {
        GameOverDialog(
            resetGame = { gameViewModel.resetStates() },
            wordChosen = uiState.wordRandomlyChosen,
            hitsCount = uiState.streakCount,
            exitGame = { onPopBack() }
        )
    }
    GameContent(
        onPopBack = onPopBack,
        uiState = uiState,
        checkUserGuess = { gameViewModel.checkUserGuess(it) },
        onSkipPressed = { gameViewModel.onSkipPressed() }
    )
}

@Composable
private fun GameContent(
    checkUserGuess: (Char) -> Unit,
    onPopBack: () -> Boolean,
    onSkipPressed: () -> Unit,
    uiState: GameViewModel.GameUiState
) {
    Log.d("Mai intra?", "iar am intrat")
    TopAppBarRow(onPopBack = onPopBack, onSkipPressed = onSkipPressed)
    Column(
        modifier = Modifier
            .background(Color.White)
            .padding(horizontal = 16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TitleText(text = uiState.question, modifier = Modifier.padding(12.dp))
        if (uiState.dashboardType != DashboardQuizType.CAPITALS.name) {
            AsyncImage(
                modifier = Modifier
                    .size(120.dp),
                contentScale = ContentScale.Fit,
                model = uiState.countryRandomFlag,
                contentDescription = null
            )
        }
        HeartAnimation(
            uiState.livesLeft
        )
        Column(modifier = Modifier.fillMaxWidth()) {
            ChosenWordRow(
                uiState.wordRandomlyChosen,
                uiState.correctLetters
            )
            TipAndCountText(tip = uiState.tipText, uiState.streakCount)
            KeyboardLayout(
                alphabetList = alphabetSet.toList(),
                checkUserGuess = { checkUserGuess(it) },
                correctLetters = uiState.correctLetters,
                usedLetters = uiState.usedLetters,
            )
        }
    }
}

@Composable
private fun ChosenWordRow(
    wordChosen: String?,
    correctLetters: Set<Char>,
) {
    println("Word chosen $wordChosen")

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        LazyRow {
            items(wordChosen!!.length) { letterIndex ->
                val currentChar = wordChosen[letterIndex]
                when {
                    currentChar.isWhitespace() -> {
                        Spacer(modifier = Modifier.padding(16.dp))
                    }

                    currentChar == '-' -> {
                        Text(
                            modifier = Modifier
                                .padding(1.2.dp)
                                .size(32.dp),
                            text = "-",
                            textAlign = TextAlign.Center,
                            fontSize = 24.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    currentChar == '\'' -> {
                        Text(
                            modifier = Modifier
                                .padding(1.2.dp)
                                .size(32.dp),
                            text = "'",
                            textAlign = TextAlign.Center,
                            fontSize = 24.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    currentChar == ',' -> {
                        Text(
                            modifier = Modifier
                                .padding(1.2.dp)
                                .size(32.dp),
                            text = ",",
                            textAlign = TextAlign.Center,
                            fontSize = 24.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    currentChar == '.' -> {
                        Text(
                            modifier = Modifier
                                .padding(1.2.dp)
                                .size(32.dp),
                            text = ".",
                            textAlign = TextAlign.Center,
                            fontSize = 24.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    else -> {
                        WordLetter(
                            letter = currentChar,
                            correctLetters = correctLetters
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TipAndCountText(tip: String, winCount: Int) {
    Column(
        modifier = Modifier
            .padding(vertical = 16.dp)
            .fillMaxWidth(),
    ) {
        Text(text = stringResource(id = R.string.TIP_TEXT, tip), color = Color.Black)
        AnimatedText(count = winCount)
    }
}

@Composable
private fun KeyboardLayout(
    checkUserGuess: (Char) -> Unit,
    alphabetList: List<Char>,
    correctLetters: Set<Char>,
    usedLetters: Set<Char>,
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(40.dp),
        modifier = Modifier
            .fillMaxWidth()
    ) {
        items(alphabetList.size) {
            val keyLetter = alphabetList[it]
            key(it) {
                KeyboardKey(
                    letterFromButton = keyLetter,
                    correctLetters = correctLetters,
                    usedLetters = usedLetters,
                    checkUserGuess = { checkUserGuess(keyLetter) }
                )
            }
        }
    }
}

@Composable
fun TopAppBarRow(
    modifier: Modifier = Modifier,
    onPopBack: () -> Boolean,
    onSkipPressed: () -> Unit
) {
    Box(modifier = modifier
        .fillMaxWidth()
        .zIndex(2f)) {
        Image(
            modifier = Modifier
                .clickable { onPopBack() }
                .align(Alignment.TopStart)
                .padding(top = 24.dp, start = 16.dp),
            painter = painterResource(id = R.drawable.ic_back),
            contentDescription = null
        )
        Text(text = stringResource(R.string.skip),
            color = Color.Black,
            fontSize = 20.sp,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .clickable { onSkipPressed() }
                .padding(top = 24.dp, end = 16.dp)
        )
    }
}