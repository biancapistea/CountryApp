package com.example.countryapp.ui.components.text

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.countryapp.R


@Composable
fun TitleText(
    modifier: Modifier = Modifier,
    text: String,
    textAlign: TextAlign = TextAlign.Center,
    fontSize: TextUnit = 32.sp,
    style: TextStyle = MaterialTheme.typography.titleLarge.copy(lineHeight = 40.sp),
    color: Color = Color.Black,
    fontWeight: FontWeight = FontWeight.Bold
    ) {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 32.dp)
            .then(modifier),
        text = text,
        textAlign = textAlign,
        fontSize = fontSize,
        style = style,
        color = color,
        fontWeight = fontWeight,
        fontFamily = FontFamily(Font(R.font.graphik_light))
    )
}