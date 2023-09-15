package com.example.countryapp.ui.components.animation

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun HeartAnimation(
    livesCount: Int
) {
    val infiniteTransition = rememberInfiniteTransition(label = "")
    val pulsate by infiniteTransition.animateFloat(
        initialValue = 35f,
        targetValue = 37f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 4000
                35f at 0 with LinearOutSlowInEasing
                37f at 200 with LinearOutSlowInEasing
                35f at 400 with LinearOutSlowInEasing
                37f at 600 with LinearOutSlowInEasing
                35f at 800 with LinearOutSlowInEasing
                35f at 4000 with LinearEasing
            },
            repeatMode = RepeatMode.Restart
        ),
        label = "",
    )
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .requiredHeight(38.dp)
            .fillMaxWidth()
    ) {
        repeat(livesCount) {
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = "Lives Player Count: $livesCount",
                tint = Color.Red,
                modifier = Modifier
                    .size(pulsate.dp)
            )
        }
    }
}