package com.example.waterfiltercompanion.ui.screen.main

import android.content.res.Configuration
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.example.waterfiltercompanion.ui.components.capacityinputdialog.CapacityInputDialog
import com.example.waterfiltercompanion.ui.components.confirmationdialog.ConfirmationDialog
import com.example.waterfiltercompanion.ui.components.consumewaterfab.ConsumeWaterFab
import com.example.waterfiltercompanion.ui.components.detailscard.DetailsCard
import com.example.waterfiltercompanion.ui.components.infobar.InfoBar
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
            Box {
                val contentPaddingModifier = Modifier.padding(16.dp)
                val configuration = LocalConfiguration.current
                when(configuration.orientation) {
                    Configuration.ORIENTATION_LANDSCAPE -> {
                        Row(contentPaddingModifier) {
                            val ringModifier = Modifier
                                .padding(top = 16.dp)
                                .fillMaxHeight()
                                .width(300.dp)
                                .weight(1f)
                            val detailsCardModifier = Modifier
                                .align(Alignment.CenterVertically)
                                .weight(1f)
                            MainContent(
                                viewModel = viewModel,
                                ringModifier = ringModifier,
                                detailsCardModifier = detailsCardModifier
                            )
                        }
                    }
                    else -> {
                        Column(contentPaddingModifier) {
                            val ringModifier = Modifier
                                .padding(top = 16.dp)
                                .fillMaxWidth()
                                .height(300.dp)
                            val detailsCardModifier = Modifier.padding(top = 96.dp)
                            MainContent(
                                viewModel = viewModel,
                                ringModifier = ringModifier,
                                detailsCardModifier = detailsCardModifier
                            )
                        }
                    }
                }
                ConsumeWaterFab(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp),
                    isShown = viewModel.consumeFabVisible,
                    onConsume = viewModel::onConsume
                )
                CapacityInputDialog(config = viewModel.capacityInputDialogConfig)
                ConfirmationDialog(config = viewModel.confirmationDialogConfig)
                InfoBar(
                    modifier = Modifier.align(Alignment.TopCenter),
                    offeredMessage = viewModel.infoBarMessage,
                    onMessageTimeout = viewModel::onInfoBarMessageTimeout
                )
            }

        }
    }
}

@ExperimentalAnimationApi
@Composable
private fun MainContent(
    viewModel: MainViewModel,
    ringModifier: Modifier,
    detailsCardModifier: Modifier
) {
    RingIndicator(
        modifier = ringModifier,
        fill = viewModel.waterFill,
        daysInUse = viewModel.daysInUse
    )
    DetailsCard(
        modifier = detailsCardModifier,
        editMode = viewModel.editMode,
        totalCapacity = viewModel.totalCapacity,
        onTotalCapacityClick = viewModel::onTotalCapacityClick,
        totalCapacityCandidate = viewModel.totalCapacityCandidate,
        remainingCapacity = viewModel.remainingCapacity,
        onRemainingCapacityClick = viewModel::onRemainingCapacityClick,
        remainingCapacityCandidate = viewModel.remainingCapacityCandidate,
        installedOnFormatted = viewModel.installedOnFormatted,
        onInstalledOnClick = viewModel::onInstalledOnClick,
        installedOnCandidateFormatted = viewModel.installedOnCandidateFormatted,
        onEdit = viewModel::onEdit,
        onCancel = viewModel::onCancel,
        onSave = viewModel::onSave,
        onClearData = viewModel::onClearData
    )
}
