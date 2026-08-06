package com.aurastream.mobile.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import com.aurastream.mobile.ui.theme.SpotifySurface
import com.aurastream.mobile.ui.theme.SpotifySurfaceVariant

@Composable
fun ShimmerLoadingEffect(modifier: Modifier = Modifier) {
    val shimmerColors = listOf(
        SpotifySurface,
        SpotifySurfaceVariant,
        SpotifySurface
    )

    val transition = rememberInfiniteTransition(label = "shimmer")
    val translateAnim = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer_anim"
    )

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset.Zero,
        end = Offset(x = translateAnim.value, y = translateAnim.value)
    )

    Column(modifier = modifier.fillMaxWidth().padding(16.dp)) {
        repeat(5) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .padding(vertical = 6.dp)
                    .background(brush = brush, shape = RoundedCornerShape(12.dp))
            )
        }
    }
}
