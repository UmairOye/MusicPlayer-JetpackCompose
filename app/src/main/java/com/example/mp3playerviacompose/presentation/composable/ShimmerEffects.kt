package com.example.mp3playerviacompose.presentation.composable

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mp3playerviacompose.R
import com.example.mp3playerviacompose.domain.utils.Constants

@Preview(showSystemUi = true)
@Composable
fun ShimmerEffect(
) {
    val shimmerColors = listOf(
        colorResource(id = R.color.white).copy(alpha = 0.3f),
        colorResource(id = R.color.white).copy(alpha = 0.5f),
        colorResource(id = R.color.white).copy(alpha = 1.0f),
        colorResource(id = R.color.white).copy(alpha = 0.5f),
        colorResource(id = R.color.white).copy(alpha = 0.3f),
    )

    val transition = rememberInfiniteTransition(label = "")
    val translateAnimation = transition.animateFloat(
        initialValue = 0f,
        targetValue = (1000 + 500).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1000,
                easing = LinearEasing,
            ),
            repeatMode = RepeatMode.Restart,
        ),
        label = "Shimmer loading animation",
    )
    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset(x = translateAnimation.value - 500, y = 0.0f),
        end = Offset(x = translateAnimation.value, y = 270f),
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(85.dp)
            .padding(vertical = Constants.MEDIUM_PADDING_1)
            .padding(horizontal = Constants.MEDIUM_PADDING)
            .background(colorResource(id = R.color.back_button_color).copy(alpha = 0.1f), RoundedCornerShape(20))
    ) {
        Spacer(
            modifier = Modifier
                .matchParentSize()
                .background(brush)
        )
    }

}