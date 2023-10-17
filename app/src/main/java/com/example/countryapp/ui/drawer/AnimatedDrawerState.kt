package com.example.countryapp.ui.drawer

import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.TransformOrigin

@Stable
interface AnimatedDrawerState {
    val drawerWidth: Float

    val drawerTranslationX: Float
    val contentScaleX: Float
    val contentScaleY: Float
    val contentTranslationX: Float
    val contentTransformOrigin: TransformOrigin

    suspend fun open()
    suspend fun close()
}