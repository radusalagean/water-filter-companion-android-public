package com.example.waterfiltercompanion.ui.components.ringindicator

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.max

private val bgStrokeWidthDp: Dp = 8.dp
private val fgStrokeWidthDp: Dp = 12.dp

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
    val fgRingAngleEdge = remember(fill) {
        180f * fill
    }
    val maxStroke = remember {
        max(bgStroke.width, fgStroke.width)
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
            startAngle = 0f,
            endAngle = 360f,
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