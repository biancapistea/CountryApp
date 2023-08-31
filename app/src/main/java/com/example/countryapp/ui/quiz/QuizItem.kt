package com.example.countryapp.ui.quiz

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.countryapp.R
import com.example.countryapp.ui.components.buttons.BlueButtonComponent
import com.example.countryapp.ui.components.buttons.CustomRadioGroup
import com.example.countryapp.ui.components.text.ParagraphTextComponent
import com.example.countryapp.ui.models.Quiz

@Composable
fun QuizItem(
    index: Int,
    quiz: Quiz,
    defaultAnswerImage: Int,
    question: String = "",
    quizSize: Int,
    isImage: Boolean,
    selectedValue: String,
    isCorrectAnswer: Boolean?,
    onClick: () -> Unit,
    setSelectedListener: (String) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .shadow(
                elevation = 10.dp,
                shape = RoundedCornerShape(26.dp),
                spotColor = colorResource(
                    id = R.color.gray_light
                )
            )
            .clip(shape = RoundedCornerShape(26.dp))
            .background(Color.White)
            .padding(horizontal = 24.dp)
            .heightIn(min = 548.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            ParagraphTextComponent(
                text = "Question ${(index + 1)}/${quizSize}",
                textAlign = TextAlign.Start,
                paddingValues = PaddingValues(start = 0.dp, top = 24.dp)
            )
            ParagraphTextComponent(
                text = "$question ${quiz.countryName}?",
                fontWeight = FontWeight.Bold,
                paddingValues = PaddingValues(
                    start = 0.dp,
                    end = 0.dp,
                    bottom = 24.dp,
                    top = 16.dp
                ),
                textAlign = TextAlign.Start,
                fontSize = 18.sp
            )
            CustomRadioGroup(
                items = quiz.answers,
                correctAnswer = quiz.correctAnswer,
                isCorrectAnswer = isCorrectAnswer,
                selectedValue = selectedValue,
                isImage = isImage,
                defaultAnswerImage = defaultAnswerImage,
                setSelected = setSelectedListener
            )
        }
        BlueButtonComponent(
            paddingValues = PaddingValues(start = 0.dp, end = 0.dp, top = 24.dp, bottom = 40.dp),
            enabled = selectedValue.isNotEmpty(),
            text = "Next",
            onClick = onClick
        )
    }
}