package com.example.countryapp.ui.components.items

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.countryapp.R

@Composable
fun TopBackAppBar(
    modifier: Modifier = Modifier,
    onPopBack: () -> Boolean,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .zIndex(2f)
    ) {
        Image(
            modifier = Modifier
                .clickable { onPopBack() }
                .align(Alignment.TopStart)
                .padding(top = 24.dp, start = 16.dp),
            painter = painterResource(id = R.drawable.ic_back),
            contentDescription = null
        )
    }
}