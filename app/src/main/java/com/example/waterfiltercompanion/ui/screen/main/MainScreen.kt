package com.example.waterfiltercompanion.ui.screen.main

import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.waterfiltercompanion.ui.components.detailscard.DetailsCard
import com.example.waterfiltercompanion.ui.components.ringindicator.RingIndicator
import com.example.waterfiltercompanion.ui.theme.WaterFilterCompanionTheme

@ExperimentalAnimationApi
@Composable
fun MainScreen(viewModel: MainViewModel) {
    WaterFilterCompanionTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxHeight(),
            color = MaterialTheme.colors.background
        ) {
            Column(Modifier.padding(16.dp)) {
                RingIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                    fill = viewModel.waterFill,
                    daysInUse = viewModel.daysInUse
                )
                DetailsCard(
                    modifier = Modifier.padding(top = 96.dp),
                    editMode = viewModel.editMode,
                    totalCapacity = viewModel.totalCapacity,
                    remainingCapacity = viewModel.remainingCapacity,
                    installedOnFormatted = viewModel.installedOnFormatted,
                    onEdit = viewModel::onEdit,
                    onCancel = viewModel::onCancel,
                    onSave = viewModel::onSave,
                    onClearData = viewModel::onClearData
                )
            }
        }
    }
}
