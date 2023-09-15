package com.example.countryapp.ui.components.keyboard

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.countryapp.R

@Composable
fun KeyboardKey(
    modifier: Modifier = Modifier,
    letterFromButton: Char,
    correctLetters: Set<Char> = emptySet(),
    usedLetters: Set<Char> = emptySet(),
    checkUserGuess: (Char) -> Unit = {},
) {
    val isEnabled = remember(
        letterFromButton,
        usedLetters
    ) { !usedLetters.contains(letterFromButton) }
    val checkCorrectness = remember(
        letterFromButton,
        correctLetters
    ) { correctLetters.contains(letterFromButton.uppercaseChar()) }

    val transitionData = updateButtonTransitionData(checkCorrectness = checkCorrectness)

    TextButton(
        onClick = {
            checkUserGuess(letterFromButton)
        },
        enabled = isEnabled,
        shape = ShapeDefaults.ExtraLarge,
        colors = ButtonDefaults.buttonColors(
            contentColor = Color.White,
            containerColor = colorResource(id = R.color.light_blue_primary),
            disabledContainerColor = transitionData.color,
            disabledContentColor = MaterialTheme.colorScheme.outline
        ),
        modifier = modifier
            .padding(4.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Text(
            text = letterFromButton.toString(),
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.fillMaxSize(),
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun updateButtonTransitionData(
    checkCorrectness: Boolean
): ButtonTransitionData {

    val targetState = when (checkCorrectness) {
        true -> ButtonState.Correct
        false -> ButtonState.Incorrect
    }
    val transition = updateTransition(
        targetState = targetState,
        label = "button state"
    )
    val color = transition.animateColor(
        label = "color"
    ) { state ->
        when (state) {
            ButtonState.Correct -> colorResource(id = R.color.correct_answer_color)
            ButtonState.Incorrect -> MaterialTheme.colorScheme.error
        }
    }
    return remember(transition) { ButtonTransitionData(color) }
}

enum class ButtonState { Correct, Incorrect }

class ButtonTransitionData(
    color: State<Color>
) {
    val color by color
}