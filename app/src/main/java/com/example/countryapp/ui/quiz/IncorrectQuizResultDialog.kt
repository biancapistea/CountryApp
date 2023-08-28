package com.example.countryapp.ui.quiz

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.example.countryapp.R
import com.example.countryapp.ui.components.buttons.BlueButtonComponent
import com.example.countryapp.ui.components.text.ParagraphTextComponent
import com.example.countryapp.ui.components.text.TitleText

@Composable
fun IncorrectQuizResultDialog(
    onGoToDashboardPressed: () -> Unit = {},
    quizViewModel: QuizViewModel
) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val uiState by produceState(
        initialValue = QuizViewModel.UiState()
    ) {
        lifecycle.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
            quizViewModel.uiState.collect { value = it }
        }
    }

    Dialog(properties = DialogProperties(usePlatformDefaultWidth = false), onDismissRequest = {}) {
        Surface(
            modifier = Modifier
                .padding(horizontal = 32.dp, vertical = 16.dp)
                .clip(RoundedCornerShape(26.dp))
                .background(Color.White)
        ) {
            Column(
                Modifier
                    .wrapContentSize()
                    .verticalScroll(rememberScrollState())
                    .background(Color.White)
            ) {
                Box(Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.align(Alignment.TopCenter)) {
                        Image(
                            modifier = Modifier
                                .padding(top = 16.dp)
                                .width(132.dp)
                                .height(132.dp),
                            painter = painterResource(id = R.drawable.ic_wrong_quiz),
                            contentDescription = null
                        )
                    }
                }
                TitleText(
                    text = stringResource(R.string.oh_no_text),
                    modifier = Modifier.padding(
                        bottom = 24.dp,
                        top = 8.dp
                    ),
                    style = MaterialTheme.typography.titleLarge.copy(lineHeight = 30.sp),
                )
                ParagraphTextComponent(
                    paddingValues = PaddingValues(
                        start = 24.dp,
                        end = 24.dp
                    ),
                    text = "That was close! You earned ${uiState.numberCorrectQuestions} points out of ${uiState.questions.size}. Keep learning and try again."
                )
                BlueButtonComponent(
                    paddingValues = PaddingValues(
                        top = 24.dp,
                        start = 24.dp,
                        end = 24.dp
                    ),
                    text = stringResource(R.string.restart_quiz),
                    onClick = { quizViewModel.restartQuizPressed() }
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 24.dp, bottom = 28.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                )
                {
                    Text(
                        modifier = Modifier
                            .padding(end = 8.dp, bottom = 8.dp)
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ) { onGoToDashboardPressed() },
                        text = stringResource(R.string.go_to_dashboard),
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.graphik_regular)),
                        color = Color.Red
                    )
                    Image(
                        modifier = Modifier
                            .wrapContentSize()
                            .padding(bottom = 6.dp),
                        painter = painterResource(id = R.drawable.ic_dashboard),
                        contentDescription = null
                    )
                }
            }
        }
    }
}