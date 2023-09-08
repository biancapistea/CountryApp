package com.example.countryapp.ui.components.section

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.countryapp.R
import com.example.countryapp.ui.components.text.TitleText

@Composable
fun SectionHeader(
    text: String,
    isExpanded: Boolean,
    onHeaderClicked: () -> Unit,
    headerImage: Int
) {
    Row(modifier = Modifier
        .clickable { onHeaderClicked() }
        .background(colorResource(id = R.color.light_blue_primary))
        .padding(vertical = 8.dp, horizontal = 16.dp)) {
        Image(
            modifier = Modifier
                .size(40.dp),
            painter = painterResource(id = headerImage),
            contentDescription = null
        )

        TitleText(
            text = text,
            color = Color.White,
            textAlign = TextAlign.Start,
            modifier = Modifier
                .weight(1.0f)
        )
        if (isExpanded) {
            Image(
                painter = painterResource(id = R.drawable.ic_expand_more),
                modifier = Modifier.size(28.dp).padding(top = 12.dp),
                contentDescription = null
            )
        } else {
            Image(
                painter = painterResource(id = R.drawable.ic_expand_less),
                modifier = Modifier.size(28.dp).padding(top = 12.dp),
                contentDescription = null
            )
        }
    }
}