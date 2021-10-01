package com.example.waterfiltercompanion.ui.components.consumewaterfab

import androidx.compose.material.FloatingActionButton
import androidx.compose.material.FloatingActionButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.waterfiltercompanion.R

@Composable
fun ConsumeWaterFab(modifier: Modifier = Modifier, isShown: Boolean, onConsume: () -> Unit) {
    if (!isShown) return
    FloatingActionButton(
        modifier = modifier,
        onClick = onConsume,
        elevation = FloatingActionButtonDefaults.elevation(0.dp, 0.dp)
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_baseline_local_drink_24),
            contentDescription = stringResource(R.string.consume_water)
        )
    }
}