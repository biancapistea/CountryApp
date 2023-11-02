package com.example.countryapp.ui.game.levels

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.countryapp.R
import com.example.countryapp.ui.components.items.TopBackAppBar

const val offSetConst = 270f
@Composable
fun GameLevelsScreen(onNavigateToHangmanGame: () -> Unit, onBackPressed: () -> Boolean) {
    TopBackAppBar(onPopBack = onBackPressed)
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
    ) {
        Box(Modifier.fillMaxSize()) {
            Image(
                modifier = Modifier.height(2000.dp),
                contentScale = ContentScale.FillHeight,
                painter = painterResource(id = R.drawable.img_forest),
                contentDescription = null
            )
            val textMeasurer = rememberTextMeasurer()
            val levelColor = colorResource(id = R.color.light_blue_primary)
            val dashColor = colorResource(id = R.color.light_blue_transparent)
            val image = painterResource(id = R.drawable.ic_locked)
            val circleLimit = 20
            Canvas(modifier = Modifier
                .fillMaxWidth()
                .zIndex(2f)
                .padding(start = 36.dp, end = 36.dp, top = 36.dp)
                .height(2000.dp)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = { tapOffset ->
                            var topOffset = 70f
                            var bottomOffset = 170f
                            for (i in 0 until circleLimit) {
                                val isEvenLevel = i % 2 == 0
                                val rect = Rect(
                                    top = topOffset,
                                    left = if (isEvenLevel) 70f else size.width - 170f,
                                    bottom = bottomOffset,
                                    right = if (isEvenLevel) 170f else size.width - 70f,
                                )
                                topOffset += offSetConst
                                bottomOffset += offSetConst
                                if (rect.contains(tapOffset)) {
                                    onNavigateToHangmanGame()
                                }
                            }
                        }
                    )
                },
                onDraw = {
                    var yOffset = 120f
                    for (i in 0 until circleLimit) {
                        val isEvenLevel = i % 2 == 0
                        drawCircle(
                            color = levelColor,
                            center = Offset(if (isEvenLevel) 120f else size.width - 120f, yOffset),
                            radius = 100f
                        )
                        drawText(
                            textMeasurer = textMeasurer,
                            text = (i + 1).toString(),
                            style = TextStyle(
                                color = Color.White,
                                fontSize = 36.sp,
                                fontWeight = FontWeight.Bold
                            ),
                            topLeft = Offset(
                                x = if (isEvenLevel) {
                                    if (i + 1 > 9) {
                                        75f
                                    } else {
                                        100f
                                    }
                                } else {
                                    if (i + 1 > 9) {
                                        size.width - 170f
                                    } else {
                                        size.width - 145f
                                    }
                                },
                                y = yOffset - 55f
                            )
                        )
                        if (i < circleLimit - 1) {
                            val strokePath = Path().apply {
                                moveTo(if (isEvenLevel) 220f else size.width - 220f, yOffset)
                                lineTo(
                                    if (isEvenLevel) size.width - 220f else 220f,
                                    yOffset + offSetConst
                                )
                            }
                            drawPath(
                                path = strokePath,
                                color = dashColor,
                                style = Stroke(
                                    width = 6.dp.toPx(),
                                    cap = StrokeCap.Round,
                                )
                            )
                            yOffset += offSetConst
                        }
                    }
                })
        }
    }
}
