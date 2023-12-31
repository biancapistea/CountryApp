package com.example.countryapp.ui.quiz

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.countryapp.R
import com.example.countryapp.ui.components.loading.IndeterminateCircularIndicator
import com.example.countryapp.ui.components.text.ParagraphTextComponent
import com.example.countryapp.ui.connectivity.ConnectivityStatus
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun QuizScreen(
    viewModel: QuizViewModel,
    onExitQuizPressed: () -> Unit = {},
    onNavigateToSuccessResultQuizDialog: () -> Unit = {},
    onNavigateToIncorrectQuizResultDialog: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    IndeterminateCircularIndicator(uiState.isLoading)

    if (uiState.restartQuiz) {
        LaunchedEffect(Unit) {
            viewModel.updateRestartQuiz()
        }
    } else if (!uiState.isLoading) {
        ConnectivityStatus {
            Quiz(
                uiState = uiState,
                onSelectedValue = viewModel::setSelectedValue,
                onAnswerCheckListener = viewModel::checkAnswer,
                onNextButtonClicked = viewModel::clearQuestion,
                onExitQuizPressed = onExitQuizPressed
            )
        }
    }

    if (uiState.isQuizFinalized && uiState.numberCorrectQuestions >= uiState.questions.size / 2) {
        LaunchedEffect(Unit) {
            onNavigateToSuccessResultQuizDialog()
        }
    } else if (uiState.isQuizFinalized) {
        LaunchedEffect(Unit) {
            onNavigateToIncorrectQuizResultDialog()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun Quiz(
    uiState: QuizViewModel.UiState,
    onSelectedValue: (String) -> Unit,
    onAnswerCheckListener: (Int, () -> Unit) -> Unit,
    onNextButtonClicked: () -> Unit,
    onExitQuizPressed: () -> Unit
) {
    Log.d(
        "De cate ori intra", "hello"
    ) //TODO: needs refactor!!, intra de prea multe ori aici. De fiecare data cand apas pe un raspuns
    Scaffold { paddingValues ->
        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .background(Color.White)
                .padding(bottom = paddingValues.calculateBottomPadding())
        ) {
            val state = rememberPagerState { uiState.questions.size }
            val scope = rememberCoroutineScope()
            HorizontalPager(
                modifier = Modifier,
                state = state,
                pageSpacing = 0.dp,
                userScrollEnabled = false,
                reverseLayout = false,
                contentPadding = PaddingValues(0.dp),
                beyondBoundsPageCount = 0,
                pageSize = PageSize.Fill,
                flingBehavior = PagerDefaults.flingBehavior(state = state),
                key = null,
                pageNestedScrollConnection = PagerDefaults.pageNestedScrollConnection(
                    Orientation.Horizontal
                ),
                pageContent = { pageNumber ->
                    Box {
                        Surface(
                            Modifier
                                .wrapContentSize()
                                .background(
                                    Color.Red
                                )
                        ) {
                            Image(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(9f / 5f)
                                    .align(Alignment.TopStart)
                                    .drawWithCache {
                                        val gradient = Brush.verticalGradient(
                                            colors = listOf(
                                                Color.Transparent,
                                                Color.Black.copy(alpha = 0.5F)
                                            ),
                                            startY = size.height,
                                            endY = 0.3f
                                        )
                                        onDrawWithContent {
                                            drawContent()
                                            drawRect(gradient, blendMode = BlendMode.Multiply)
                                        }
                                    },
                                contentScale = ContentScale.Crop,
                                painter = painterResource(id = uiState.quizHeaderImage),
                                contentDescription = null,
                            )
                        }
                        Column(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(start = 32.dp, end = 32.dp, top = 136.dp),
                        ) {
                            QuizItem(
                                question = uiState.quizQuestion,
                                isImage = uiState.shouldShowImageAnswers,
                                index = pageNumber,
                                quiz = uiState.questions[pageNumber],
                                quizSize = uiState.questions.size,
                                selectedValue = uiState.selectedValue,
                                isCorrectAnswer = uiState.isCorrect,
                                defaultAnswerImage = uiState.defaultAnswerImage,
                                onClick = {
                                    onAnswerCheckListener(pageNumber)
                                    {
                                        scope.launch {
                                            delay(800L)
                                            if (uiState.questions.size != pageNumber + 1) {
                                                onNextButtonClicked()
                                                state.animateScrollToPage(state.currentPage + 1)
                                            }
                                        }
                                    }
                                },
                                setSelectedListener = onSelectedValue
                            )

                            ParagraphTextComponent(
                                modifier = Modifier
                                    .wrapContentSize()
                                    .clickable(
                                        indication = null,
                                        interactionSource = remember { MutableInteractionSource() }
                                    ) { onExitQuizPressed() },
                                text = stringResource(R.string.exit_quiz),
                                color = Color.Blue,
                                paddingValues = PaddingValues(top = 40.dp, bottom = 24.dp)
                            )
                        }
                    }
                }
            )
        }
    }
}