package net.matsudamper.shareoutside.bluebird.compose.lib

import androidx.annotation.FloatRange
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.SnapSpec
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasurePolicy
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
public fun rememberTabLayoutState(): TabLayoutState {
    return rememberSaveable(
        inputs = arrayOf(),
        saver = listSaver(
            save = { listOf(it.animatableOffset.value) },
            restore = {
                TabLayoutState(
                    initialValue = it[0],
                )
            },
        ),
    ) {
        TabLayoutState(
            initialValue = 0f,
        )
    }
}

@Stable
public class TabLayoutState(
    initialValue: Float,
) {

    internal val animatableOffset = Animatable(
        initialValue = initialValue,
        typeConverter = Float.VectorConverter,
    )
    internal val offset: Float by animatableOffset.asState()

    public suspend fun snapTo(
        selectedIndex: Int,
        @FloatRange(from = 0.0, to = 1.0) fraction: Float = 0.0f,
    ) {
        if (animatableOffset.isRunning) return
        animatableOffset.animateTo(
            targetValue = selectedIndex + fraction,
            animationSpec = SnapSpec(0),
        )
    }

    public suspend fun animateTo(
        selectedIndex: Int,
        @FloatRange(from = 0.0, to = 1.0) fraction: Float = 0.0f,
    ) {
        animatableOffset.animateTo(
            targetValue = selectedIndex + fraction,
            animationSpec = tween(durationMillis = 250, easing = FastOutSlowInEasing),
        )
    }
}

@Composable
internal fun TabLayout(
    modifier: Modifier,
    size: Int,
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = contentColorFor(backgroundColor),
    state: TabLayoutState = rememberTabLayoutState(),
    indicator: @Composable () -> Unit = {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(3.dp)
                .background(contentColor),
        )
    },
    content: @Composable (index: Int) -> Unit,
) {
    var enableScroll by rememberSaveable {
        mutableStateOf(false)
    }
    Surface(
        modifier = modifier,
        color = backgroundColor,
        contentColor = contentColor,
    ) {
        BoxWithConstraints(
            modifier = Modifier.fillMaxWidth(),
        ) {
            val containerWidthPx = with(LocalDensity.current) { maxWidth.toPx() }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(state = rememberScrollState(), enabled = enableScroll),
                contentAlignment = Alignment.BottomStart,
            ) {
                var indicatorWidth by rememberSaveable { mutableStateOf(0) }
                Layout(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Min),
                    content = {
                        (0 until size).forEach { index ->
                            content(index)
                        }
                    },
                    measurePolicy = object : MeasurePolicy {
                        override fun MeasureScope.measure(measurables: List<Measurable>, constraints: Constraints): MeasureResult {
                            val contentMaxOfMaxIntrinsicWidth = measurables.map { it.maxIntrinsicWidth(constraints.maxHeight) }
                                .maxOfOrNull { it } ?: return layout(0, 0) {}

                            val contentWidth: Int
                            if (contentMaxOfMaxIntrinsicWidth * measurables.size > containerWidthPx) {
                                enableScroll = true
                                contentWidth = contentMaxOfMaxIntrinsicWidth
                            } else {
                                enableScroll = false
                                contentWidth = (containerWidthPx / measurables.size).toInt()
                            }
                            indicatorWidth = contentWidth

                            val contentMaxIntrinsicHeight = measurables.map { it.maxIntrinsicHeight(contentWidth) }
                                .maxOfOrNull { it } ?: return layout(0, 0) {}

                            val placeables = measurables.map {
                                it.measure(Constraints.fixed(width = contentWidth, height = contentMaxIntrinsicHeight))
                            }
                            return layout(contentWidth * measurables.size, contentMaxIntrinsicHeight) {
                                placeables.forEachIndexed { index, placeable ->
                                    placeable.place(x = contentWidth * index, y = 0)
                                }
                            }
                        }
                    },
                )

                val value by remember {
                    derivedStateOf {
                        (indicatorWidth * state.offset).toInt()
                    }
                }

                Box(
                    modifier = Modifier
                        .width(
                            with(LocalDensity.current) {
                                indicatorWidth.toDp()
                            },
                        )
                        .offset {
                            IntOffset(x = value, y = 0)
                        },
                ) {
                    indicator()
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    val coroutineScope = rememberCoroutineScope()
    Column(modifier = Modifier.fillMaxSize()) {
        (0..10).forEach { index ->
            TabLayout(
                modifier = Modifier.padding(bottom = 4.dp),
                state = rememberTabLayoutState().apply {
                    coroutineScope.launch {
                        snapTo(0, index / 10f)
                    }
                },
                size = 3,
                content = {
                    Box(
                        modifier = Modifier
                            .padding(12.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(text = "Tab $it")
                    }
                },
                indicator = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(3.dp)
                            .background(Color.Yellow),
                    )
                }
            )
        }
    }
}