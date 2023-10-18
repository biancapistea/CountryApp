package com.example.countryapp.ui.drawer

import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.countryapp.R

@Composable
fun AnimatedDrawer(
    modifier: Modifier = Modifier,
    state: AnimatedDrawerState = rememberAnimatedDrawerState(
        drawerWidth = 280.dp,
    ),
    drawerContent: @Composable () -> Unit,
    content: @Composable () -> Unit,
) {
    Layout(
        modifier = Modifier
            .background(color = colorResource(id = R.color.light_blue_primary))
            .then(modifier),
        content = {
            drawerContent()
            content()
        }
    ) { measure, constraints ->
        val (drawerContentMeasurable, contentMeasurable) = measure
        val drawerContentConstraints = Constraints.fixed(
            width = state.drawerWidth.coerceAtMost(constraints.maxWidth.toFloat()).toInt(),
            height = constraints.maxHeight,
        )
        val drawerContentPlaceable = drawerContentMeasurable.measure(drawerContentConstraints)
        val contentConstraints = Constraints.fixed(
            width = constraints.maxWidth,
            height = constraints.maxHeight,
        )
        val contentPlaceable = contentMeasurable.measure(contentConstraints)
        layout(
            width = constraints.maxWidth,
            height = constraints.maxHeight,
        ) {
            contentPlaceable.placeRelativeWithLayer(
                IntOffset.Zero,
            ) {
                transformOrigin = state.contentTransformOrigin
                scaleX = state.contentScaleX
                scaleY = state.contentScaleY
                translationX = state.contentTranslationX
            }
            drawerContentPlaceable.placeRelativeWithLayer(
                IntOffset.Zero,
            ) {
                translationX = state.drawerTranslationX
            }
        }
    }
}
