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
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.example.countryapp.R
import com.example.countryapp.ui.components.text.ParagraphTextComponent
import com.example.countryapp.ui.dashboard.DashboardQuizType
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun QuizScreen(
    viewModel: QuizViewModel,
    type: String,
    onExitQuizPressed: () -> Unit = {},
    onNavigateToSuccessResultQuizDialog: () -> Unit = {},
    onNavigateToIncorrectQuizResultDialog: () -> Unit = {}
) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val uiState by produceState(
        initialValue = QuizViewModel.UiState()
    ) {
        lifecycle.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
            viewModel.uiState.collect { value = it }
        }
    }

    if (uiState.restartQuiz) {
        LaunchedEffect(Unit) {
            viewModel.updateRestartQuiz()
        }
    } else {
        Quiz(
            uiState = uiState,
            onSelectedValue = viewModel::setSelectedValue,
            onAnswerCheckListener = viewModel::checkAnswer,
            onNextButtonClicked = viewModel::clearQuestion,
            onExitQuizPressed = onExitQuizPressed,
            type = type
        )
    }

    if (uiState.isQuizFinalized && uiState.numberCorrectQuestions == uiState.questions.size) {
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
    onAnswerCheckListener: (Int, () -> Unit, () -> Unit) -> Unit,
    onNextButtonClicked: () -> Unit,
    onExitQuizPressed: () -> Unit,
    type: String,
) {
    Log.d("De cate ori intra", "hello")
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
                                painter = if (type == DashboardQuizType.FLAGS.name) {
                                    painterResource(id = R.drawable.image_country_flags_header)
                                } else {
                                    painterResource(id = R.drawable.ic_capitals_world)
                                },
                                contentDescription = null,
                            )
                        }
                        Column(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(start = 32.dp, end = 32.dp, top = 136.dp),
                        ) {
                            QuizItem(
                                question = if (type == DashboardQuizType.FLAGS.name) {
                                    "What is the flag for"
                                } else {
                                    "What is the capital of"
                                },
                                isImage = type == DashboardQuizType.FLAGS.name,
                                index = pageNumber,
                                quiz = uiState.questions[pageNumber],
                                quizSize = uiState.questions.size,
                                selectedValue = uiState.selectedValue,
                                isCorrectAnswer = uiState.isCorrect,
                                onClick = {
                                    onAnswerCheckListener(pageNumber, onExitQuizPressed)
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
                                text = "Exit quiz",
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