package com.example.countryapp.ui.components.section

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.runtime.toMutableStateMap
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.countryapp.R
import com.example.countryapp.ui.components.customgriditems.gridItems
import com.example.countryapp.ui.components.items.CountryItem

@Composable
fun ExpandableList(sections: List<SectionData>, explainingMessage: String = "") {
    val isExpandedMap = rememberSavableSnapshotStateMap {
        List(sections.size) { index: Int -> index to false }
            .toMutableStateMap()
    }

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        content = {
            item {
                Text(
                    text = explainingMessage,
                    color = Color.Black,
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.graphik_regular)),
                    style = MaterialTheme.typography.titleLarge.copy(lineHeight = 22.sp),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 32.dp, end = 32.dp, top = 64.dp, bottom = 8.dp)
                )
            }
            sections.onEachIndexed { index, sectionData ->
                Section(
                    sectionData = sectionData,
                    isExpanded = isExpandedMap[index] ?: false,
                    onHeaderClick = {
                        isExpandedMap[index] = !(isExpandedMap[index] ?: true)
                    }
                )
            }
        }
    )
}

fun <K, V> snapshotStateMapSaver() = Saver<SnapshotStateMap<K, V>, Any>(
    save = { state -> state.toList() },
    restore = { value ->
        @Suppress("UNCHECKED_CAST")
        (value as? List<Pair<K, V>>)?.toMutableStateMap() ?: mutableStateMapOf<K, V>()
    }
)

@Composable
fun <K, V> rememberSavableSnapshotStateMap(init: () -> SnapshotStateMap<K, V>): SnapshotStateMap<K, V> =
    rememberSaveable(saver = snapshotStateMapSaver(), init = init)

fun LazyListScope.Section(
    sectionData: SectionData,
    isExpanded: Boolean,
    onHeaderClick: () -> Unit
) {

    item {
        SectionHeader(
            text = sectionData.headerText,
            isExpanded = isExpanded,
            onHeaderClicked = onHeaderClick,
            headerImage = sectionData.headerImage
        )
    }

    if (isExpanded) {
        gridItems(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(
                end = 16.dp,
                start = 16.dp,
                top = 16.dp,
                bottom = 16.dp
            ),
            data = sectionData.items,
            columnCount = 3,
            itemContent = { itemData ->
                CountryItem(itemData)
            },
            hasMultipleLines = true,
            onItemClick = { position ->
                sectionData.onItemClick(position)
            }
        )
    }
}