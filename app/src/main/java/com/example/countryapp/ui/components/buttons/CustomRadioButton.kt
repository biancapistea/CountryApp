package com.example.countryapp.ui.components.buttons

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.countryapp.R
import com.example.countryapp.ui.components.text.ParagraphTextComponent

@Composable
fun CustomRadioGroup(
    items: List<String>,
    isCorrectAnswer: Boolean?,
    correctAnswer: String,
    selectedValue: String,
    isImage: Boolean,
    defaultAnswerImage: Int,
    setSelected: (String) -> Unit = {},
) {
    items.forEach { item ->
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .border(
                    width = 1.dp,
                    color = colorResource(
                        id = getBorderColor(
                            selectedValue == item,
                            isCorrectAnswer,
                            correctAnswer == item
                        )
                    ),
                    shape = CircleShape
                )
                .background(
                    color = colorResource(
                        id = getBackgroundColor(
                            selectedValue == item,
                            isCorrectAnswer,
                            correctAnswer == item
                        )
                    ), shape = CircleShape
                )
                .clickable(indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) { setSelected(item) },
            verticalAlignment = Alignment.CenterVertically,
        ) {
            val isValueSelected = selectedValue == item || correctAnswer == item
            if (isValueSelected && isCorrectAnswer != null) {
                Image(
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(start = 18.dp, bottom = 12.dp, end = 12.dp, top = 12.dp),
                    painter = painterResource(id = getIcon(isCorrectAnswer, correctAnswer == item)),
                    contentDescription = null
                )
            } else {
                RadioButton(
                    modifier = Modifier.padding(start = 6.dp),
                    enabled = isCorrectAnswer == null,
                    selected = (selectedValue == item),
                    onClick = { setSelected(item) },
                    colors = RadioButtonDefaults.colors(
                        unselectedColor = colorResource(id = R.color.gray_light),
                        selectedColor = Color.Red,
                    )
                )
            }
            if (isImage) {
                if (item == "Does not have") {
                    Image(
                        modifier = Modifier
                            .width(90.dp)
                            .height(60.dp)
                            .aspectRatio(5f / 3f),
                        painter = painterResource(id = defaultAnswerImage),
                        contentDescription = null
                    )
                } else {
                    AsyncImage(
                        modifier = Modifier
                            .width(90.dp)
                            .height(66.dp)
                            .aspectRatio(5f / 3f),
                        model = item,
                        contentDescription = null
                    )
                }
            } else {
                ParagraphTextComponent(
                    text = item,
                    paddingValues = PaddingValues(
                        start = 4.dp,
                        end = 8.dp,
                        top = 10.dp,
                        bottom = 12.dp
                    ),
                    textAlign = TextAlign.Start,
                    style = MaterialTheme.typography.titleLarge.copy(lineHeight = 22.sp)
                )
            }
        }
    }
}

private fun getBorderColor(isSelected: Boolean, isCorrectAnswer: Boolean?, shouldShowCorrectAnswer: Boolean): Int {
    if (isCorrectAnswer == false && shouldShowCorrectAnswer) return R.color.correct_answer_color
    if (isSelected) {
        return when (isCorrectAnswer) {
            false -> R.color.red
            true -> R.color.correct_answer_color
            else -> R.color.red
        }
    }
    return R.color.gray_light
}

private fun getBackgroundColor(isSelected: Boolean, isCorrectAnswer: Boolean?, shouldShowCorrectAnswer: Boolean): Int {
    if (isCorrectAnswer == false && shouldShowCorrectAnswer) return R.color.correct_answer_color
    if (isSelected) {
        return when (isCorrectAnswer) {
            false -> R.color.red
            true -> R.color.correct_answer_color
            else -> R.color.gray_light_background_quiz
        }
    }
    return R.color.gray_light_background_quiz
}

private fun getIcon(isCorrectAnswer: Boolean?, shouldShowCorrectAnswer: Boolean): Int {
    if (isCorrectAnswer == false && shouldShowCorrectAnswer) return R.drawable.ic_correct_answer
    return when (isCorrectAnswer) {
        true -> R.drawable.ic_correct_answer
        else -> R.drawable.ic_wrong_answer
    }
}