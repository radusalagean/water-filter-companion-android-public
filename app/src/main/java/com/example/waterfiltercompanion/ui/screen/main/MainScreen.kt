package com.example.waterfiltercompanion.ui.screen.main

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.waterfiltercompanion.ui.components.detailscard.DetailsCard
import com.example.waterfiltercompanion.ui.components.ringindicator.Ring
import com.example.waterfiltercompanion.ui.components.ringindicator.RingIndicator
import com.example.waterfiltercompanion.ui.theme.ColorRingBackground
import com.example.waterfiltercompanion.ui.theme.ColorRingForeground
import com.example.waterfiltercompanion.ui.theme.WaterFilterCompanionTheme
import com.example.waterfiltercompanion.ui.utils.debugBorder

@Composable
fun MainScreen(viewModel: MainViewModel) {
    WaterFilterCompanionTheme {
        // A surface container using the 'background' color from the theme
        Surface(modifier = Modifier.fillMaxHeight(), color = MaterialTheme.colors.background) {
            Column {
                RingIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                    fill = 1f,
                    daysInUse = null
                )
                DetailsCard(modifier = Modifier.padding(top = 96.dp), editState = true)
            }
        }
    }
}