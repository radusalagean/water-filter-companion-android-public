package com.example.waterfiltercompanion.ui.components.ringindicator

import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.max

private val bgStrokeWidthDp: Dp = 8.dp
private val fgStrokeWidthDp: Dp = 12.dp

private enum class TransitionState { INIT_START, INIT_END, FILLED }

@Composable
fun Ring(
    modifier: Modifier = Modifier,
    bgColor: Color,
    fgColor: Color,
    fill: Float
) {
    var bgStroke: Stroke
    var fgStroke: Stroke
    with(LocalDensity.current) {
        bgStroke = remember {
            Stroke(width = bgStrokeWidthDp.toPx())
        }
        fgStroke = remember {
            Stroke(
                width = fgStrokeWidthDp.toPx(),
                cap = StrokeCap.Round
            )
        }
    }
    val maxStroke = remember {
        max(bgStroke.width, fgStroke.width)
    }
    val transitionState = remember {
        MutableTransitionState(TransitionState.INIT_START)
    }
    val transition = updateTransition(
        transitionState = transitionState,
        label = "ring-anim-transition"
    )
    val bgRingAngleEdge by transition.animateFloat(
        transitionSpec = {
            tween(
                durationMillis = 400,
                delayMillis = 400
            )
        },
        label = "bgRingAngleEdge"
    ) { currentState ->
        if (currentState == TransitionState.INIT_START) 0f else 180f
    }
    val fgRingAngleEdge by transition.animateFloat(
        transitionSpec = {
            tween(durationMillis = 400)
        },
        label = "fgRingAngleEdge"
    ) { currentState ->
        if (currentState == TransitionState.FILLED) 180f * fill else 0f
    }
    LaunchedEffect(transitionState.currentState) {
        transitionState.targetState = when(transitionState.currentState) {
            TransitionState.INIT_START -> TransitionState.INIT_END
            else -> TransitionState.FILLED
        }
    }
    Canvas(
        modifier
            .fillMaxWidth()
            .height(300.dp)) {
        val innerRadius = (size.minDimension - maxStroke) / 2
        val halfSize = size / 2f
        val topLeft = Offset(
            x = halfSize.width - innerRadius,
            y = halfSize.height - innerRadius
        )
        val arcSize = Size(width = innerRadius * 2, height = innerRadius * 2)
        // Background Ring
        drawRing(
            color = bgColor,
            startAngle = -bgRingAngleEdge,
            endAngle = bgRingAngleEdge,
            topLeft = topLeft,
            size = arcSize,
            style = bgStroke
        )
        // Foreground Ring
        drawRing(
            color = fgColor,
            startAngle = 180 - fgRingAngleEdge,
            endAngle = 180 + fgRingAngleEdge,
            topLeft = topLeft,
            size = arcSize,
            style = fgStroke
        )
    }
}

private fun DrawScope.drawRing(
    color: Color,
    startAngle: Float,
    endAngle: Float,
    topLeft: Offset,
    size: Size,
    style: DrawStyle
) {
    drawArc(
        color = color,
        startAngle = startAngle - 90f,
        sweepAngle = endAngle - startAngle,
        useCenter = false,
        topLeft = topLeft,
        size = size,
        style = style
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewRing() {
    Ring(
        modifier = Modifier.size(300.dp),
        fill = 0.8f,
        bgColor = Color.DarkGray,
        fgColor = Color.Cyan
    )
}