package com.example.waterfiltercompanion.ui.screen.main

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.example.waterfiltercompanion.ui.components.ringindicator.Ring
import com.example.waterfiltercompanion.ui.theme.ColorRingBackground
import com.example.waterfiltercompanion.ui.theme.ColorRingForeground
import com.example.waterfiltercompanion.ui.theme.WaterFilterCompanionTheme

@Composable
fun MainScreen(viewModel: MainViewModel) {
    WaterFilterCompanionTheme {
        // A surface container using the 'background' color from the theme
        Surface(color = MaterialTheme.colors.background) {
            Ring(
                bgColor = MaterialTheme.colors.ColorRingBackground,
                fgColor = MaterialTheme.colors.ColorRingForeground,
                fill = 0.5f
            )
        }
    }
}