package com.example.countryapp.ui.components.buttons

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.countryapp.R
import java.text.Normalizer

private const val ALPHA_CORRECT_VALUE = 1F
private const val ALPHA_INCORRECT_VALUE = 0F

@Composable
@Stable
fun WordLetter(
    modifier: Modifier = Modifier,
    letter: Char,
    correctLetters: Set<Char> = emptySet()
) {

    val letterNormalized = Normalizer.normalize(letter.toString(), Normalizer.Form.NFD)
    val normalizedChar: Char = letterNormalized[0]
    val isLetterCorrect: Boolean = correctLetters.contains(normalizedChar)
    val alphaValue = if (isLetterCorrect) ALPHA_CORRECT_VALUE else ALPHA_INCORRECT_VALUE
    val alphaAnimation: Float by animateFloatAsState(
        targetValue = alphaValue,
        animationSpec = tween(durationMillis = 300, easing = LinearOutSlowInEasing), label = ""
    )
    val haveAnimationOrNot = if (alphaValue == ALPHA_CORRECT_VALUE) alphaAnimation else alphaValue

    Card(
        modifier = modifier
            .padding(2.4.dp)
            .size(32.dp),
        elevation = CardDefaults.elevatedCardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(id = R.color.light_blue_primary),
            contentColor = Color.White,
        )
    ) {
        Text(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .alpha(haveAnimationOrNot),
            text = letter.toString().uppercase(),
            textAlign = TextAlign.Center,
            fontSize = 24.sp
        )
    }
}