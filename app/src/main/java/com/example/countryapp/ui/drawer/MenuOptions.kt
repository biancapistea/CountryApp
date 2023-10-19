package com.example.countryapp.ui.drawer

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.countryapp.R
import kotlinx.coroutines.launch

@Composable
fun <T> MenuOptions(
    modifier: Modifier = Modifier,
    onCloseClick: () -> Unit,
    menuItems: List<AppDrawerItemInfo<T>>,
    defaultPick: T,
    onClick: (T) -> Unit,
    drawerState: AnimatedDrawerState
) {
    Surface(
        modifier = modifier,
        color = colorResource(id = R.color.light_blue_primary),
    ) {
        var currentPick by remember { mutableStateOf(defaultPick) }
        val coroutineScope = rememberCoroutineScope()
        Column {
            IconButton(
                onClick = onCloseClick,
                modifier = Modifier
                    .padding(all = 16.dp)
            ) {
                Icon(imageVector = Icons.Default.Close, contentDescription = "close")
            }
            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(top = 12.dp)
            ) {
                itemsIndexed(menuItems) { _, item ->
                    AppDrawerItem(
                        item = item,
                        color = colorResource(id = R.color.light_blue_primary),
                        selectedOption = currentPick,
                    ) { navOption ->

                        if (currentPick == navOption) {
                            return@AppDrawerItem
                        }

                        currentPick = navOption

                        coroutineScope.launch {
                            drawerState.close()
                        }

                        onClick(navOption)
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> AppDrawerItem(
    color: Color = colorResource(id = R.color.light_blue_transparent),
    selectedOption: T,
    item: AppDrawerItemInfo<T>,
    onClick: (options: T) -> Unit,
) {
    Surface(
        modifier = Modifier
            .padding(horizontal = 12.dp)
            .clip(
                if (selectedOption == item.drawerOption) RoundedCornerShape(
                    topEnd = 18.dp,
                    bottomEnd = 18.dp
                ) else RoundedCornerShape(
                    0.dp
                )
            ),
        color = if (selectedOption == item.drawerOption) colorResource(id = R.color.menu_item_selected_background) else color,
        onClick = { onClick(item.drawerOption) }
    ) {
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Image(
                modifier = Modifier
                    .padding(end = 16.dp)
                    .size(26.dp),
                painter = painterResource(id = item.drawableId),
                contentDescription = null
            )
            Text(
                modifier = Modifier.padding(top = 2.dp),
                text = stringResource(id = item.title),
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = Color.White
            )
        }
    }
}