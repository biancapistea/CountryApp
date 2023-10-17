package com.example.countryapp.ui.drawer

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.TransformOrigin

private const val AnimationDurationMillis = 600

@Stable
class AnimatedDrawerStateImpl(
    override val drawerWidth: Float,
) : AnimatedDrawerState {

    private val animation = Animatable(0f)

    override val drawerTranslationX: Float
        get() = -drawerWidth * (1f - animation.value)

    override val contentScaleX: Float
        get() = 1f - .2f * animation.value

    override val contentScaleY: Float
        get() = 1f - .2f * animation.value

    override val contentTranslationX: Float
        get() = drawerWidth * animation.value

    override val contentTransformOrigin: TransformOrigin
        get() = TransformOrigin(pivotFractionX = 0f, pivotFractionY = .5f)

    override suspend fun open() {
        animation.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = AnimationDurationMillis,
            )
        )
    }

    override suspend fun close() {
        animation.animateTo(
            targetValue = 0f,
            animationSpec = tween(
                durationMillis = AnimationDurationMillis,
            )
        )
    }
}