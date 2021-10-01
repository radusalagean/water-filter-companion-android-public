package com.example.waterfiltercompanion.ui.components.ringindicator

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.waterfiltercompanion.R
import com.example.waterfiltercompanion.ui.theme.ColorRingBackground
import com.example.waterfiltercompanion.ui.theme.ColorRingForeground
import com.example.waterfiltercompanion.ui.utils.quantityStringResourceWithFallback
import kotlinx.coroutines.channels.Channel
import kotlin.math.roundToInt

@Composable
fun RingIndicator(
    modifier: Modifier = Modifier,
    fill: Float,
    daysInUse: Int?,
    recompositionChannel: (Channel<Unit>)? = null
) {
    var percentage by remember { mutableStateOf(0) }
    ConstraintLayout(modifier) {
        val (percentageRef, daysInUseRef, labelRef) = createRefs()
        Ring(
            modifier = Modifier.fillMaxSize(),
            bgColor = MaterialTheme.colors.ColorRingBackground,
            fgColor = MaterialTheme.colors.ColorRingForeground,
            fill = fill,
            fgFillCb = { fgFill ->
                percentage = (fgFill * 100).roundToInt()
            },
            recompositionChannel = recompositionChannel
        )
        Text(
            modifier = Modifier.constrainAs(percentageRef) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            },
            text = stringResource(R.string.ring_indicator_remaining_capacity_percentage_format, percentage),
            style = MaterialTheme.typography.h2,
            fontWeight = FontWeight.Bold
        )
        Text(
            modifier = Modifier.constrainAs(daysInUseRef) {
                bottom.linkTo(percentageRef.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            },
            text = quantityStringResourceWithFallback(
                R.plurals.ring_indicator_days_in_use_format,
                daysInUse
            ).uppercase(),
            style = MaterialTheme.typography.subtitle1
        )
        Text(
            modifier = Modifier.constrainAs(labelRef) {
                top.linkTo(percentageRef.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            },
            text = stringResource(R.string.ring_indicator_remaining_capacity_label).uppercase(),
            style = MaterialTheme.typography.overline,
            fontSize = 12.sp
        )
    }
}