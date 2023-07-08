package net.matsudamper.shareoutside.bluebird.compose.lib

import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import kotlin.math.floor

@Composable
public fun Indicator(
    modifier: Modifier = Modifier,
    count: Int,
    size: Dp = 8.dp,
    indicatorPadding: Dp = 4.dp,
    currentIndex: () -> Float,
    activeColor: Color = MaterialTheme.colorScheme.primary,
    inActiveColor: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
    easing: Easing = LinearEasing,
) {
    val reverseEasing = remember(easing) {
        Easing { 1 - easing.transform(1 - it) }
    }
    Box(modifier = modifier.width(IntrinsicSize.Min)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
        ) {
            repeat(count) { index ->
                Box(
                    Modifier
                        .size(size)
                        .then(
                            Modifier
                                .background(inActiveColor, CircleShape),
                        ),
                )
                if (index != count - 1) {
                    Spacer(modifier = Modifier.width(indicatorPadding))
                }
            }
        }
        Canvas(
            modifier = Modifier.fillMaxWidth(),
            onDraw = {
                val a = 2

                val currentIndexValue = currentIndex()
                val offsetXFactor: Float
                val widthFactor: Float
                val indexRemainder = currentIndexValue % 1
                if (indexRemainder < 0.5) {
                    widthFactor = easing.transform(a * indexRemainder)
                    offsetXFactor = floor(currentIndexValue)
                } else {
                    widthFactor = reverseEasing.transform(1 - (a * (indexRemainder - 0.5f)))
                    offsetXFactor = floor(currentIndexValue) + easing.transform((a * (indexRemainder - 0.5f)))
                }

                drawRoundRect(
                    color = activeColor,
                    topLeft = Offset(offsetXFactor * (size + indicatorPadding).toPx(), 0f),
                    size = Size((widthFactor * (size + indicatorPadding) + size).toPx(), size.toPx()),
                    cornerRadius = CornerRadius(size.toPx() / 2),
                )
            },
        )
    }
}