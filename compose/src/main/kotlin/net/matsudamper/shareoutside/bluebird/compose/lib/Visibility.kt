package net.matsudamper.shareoutside.bluebird.compose.lib

import androidx.compose.runtime.Composable
import androidx.compose.ui.layout.Layout

@Composable
internal fun Visibility(
    visible: Boolean,
    content: @Composable () -> Unit,
) {
    Layout(
        content = content,
        measurePolicy = { measurables, constraints ->
            val placeable = measurables.first().measure(constraints)
            layout(placeable.width, placeable.height) {
                if (visible) {
                    placeable.placeRelative(0, 0)
                }
            }
        },
    )
}