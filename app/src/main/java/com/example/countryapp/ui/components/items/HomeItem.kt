package com.example.countryapp.ui.components.items

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.countryapp.R
import com.example.countryapp.ui.components.text.ParagraphTextComponent
import com.example.countryapp.ui.components.text.TitleText

@Composable
fun HomeItem(
    headerImage: Int,
    description: String,
    actionText: String,
    title: String,
    onClickOnActionText: () -> Unit
) {
    Column(modifier = Modifier.wrapContentSize()) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                modifier = Modifier
                    .fillMaxSize()
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
                painter = painterResource(id = headerImage),
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
            ) {
                TitleText(
                    text = title,
                    textAlign = TextAlign.Start, color = Color.White
                )
                Column(
                    modifier = Modifier
                        .padding(start = 32.dp, end = 32.dp, top = 12.dp, bottom = 32.dp)
                        .shadow(
                            elevation = 10.dp,
                            shape = RoundedCornerShape(26.dp),
                            spotColor = colorResource(
                                id = R.color.gray_light
                            )
                        )
                        .clip(shape = RoundedCornerShape(26.dp))
                        .background(Color.White)
                        .heightIn(min = 120.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    ParagraphTextComponent(
                        text = description,
                        textAlign = TextAlign.Start,
                        paddingValues = PaddingValues(start = 16.dp, end = 16.dp, top = 24.dp)
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.CenterHorizontally)
                            .padding(top = 24.dp, bottom = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.End
                    ) {
                        Text(
                            modifier = Modifier
                                .padding(end = 8.dp, bottom = 8.dp)
                                .clickable(
                                    indication = null,
                                    interactionSource = remember { MutableInteractionSource() }
                                ) { onClickOnActionText() },
                            text = actionText,
                            fontSize = 14.sp,
                            fontFamily = FontFamily(Font(R.font.graphik_regular)),
                            color = colorResource(id = R.color.light_blue_primary)
                        )
                        Image(
                            modifier = Modifier
                                .wrapContentSize()
                                .padding(bottom = 6.dp, end = 12.dp),
                            painter = painterResource(id = R.drawable.ic_back_home),
                            contentDescription = null
                        )
                    }
                }
            }
        }
    }
}