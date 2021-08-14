package foo.bar.clean.ui.common.anim

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn

fun fadeInAfterDelay(): EnterTransition {
    return fadeIn(
        animationSpec = tween(
            durationMillis = 5000,
            easing = CustomEasing.lateRiser,
           // delayMillis = 0,
        ),
    )
}
