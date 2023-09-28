package com.example.countryapp.ui.components.items

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.countryapp.R
import com.example.countryapp.ui.dashboard.DashboardViewModel

@Composable
fun DashboardItem(dashboardUiType: DashboardViewModel.DashboardUiType) {
    Box(
        modifier = Modifier.wrapContentSize()
    ) {
        Column {
            Image(
                painter = painterResource(id = dashboardUiType.teaserImage),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .width(142.dp)
                    .height(142.dp)
                    .clip(RoundedCornerShape(6.dp)),
            )
            Text(
                text = stringResource(id = dashboardUiType.title),
                color = Color.Black,
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.graphik_regular)),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .width(142.dp)
                    .padding(top = 10.dp)
                    .fillMaxWidth()
            )
        }
    }
}
