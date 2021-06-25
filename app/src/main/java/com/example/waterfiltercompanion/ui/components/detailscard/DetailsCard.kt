package com.example.waterfiltercompanion.ui.components.detailscard

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.waterfiltercompanion.R
import com.example.waterfiltercompanion.ui.components.detailscard.content.DetailsContent

@ExperimentalAnimationApi
@Composable
fun DetailsCard(
    modifier: Modifier = Modifier,
    editMode: Boolean,
    // Total Capacity
    totalCapacity: Int?,
    onTotalCapacityClick: () -> Unit,
    totalCapacityCandidate: String?,
    // Remaining Capacity
    remainingCapacity: Int?,
    onRemainingCapacityClick: () -> Unit,
    remainingCapacityCandidate: String?,
    // Installed On
    installedOnFormatted: String?,
    onInstalledOnClick: () -> Unit,
    installedOnCandidateFormatted: String?,
    // Callbacks
    onEdit: () -> Unit,
    onClearData: () -> Unit,
    onCancel: () -> Unit,
    onSave: () -> Unit
) {
    Card(modifier) {
        Column(Modifier.padding(8.dp)) {
            // Details Content
            DetailsContent(
                editMode = editMode,
                totalCapacity = totalCapacity,
                onTotalCapacityClick = onTotalCapacityClick,
                totalCapacityCandidate = totalCapacityCandidate,
                remainingCapacity = remainingCapacity,
                onRemainingCapacityClick = onRemainingCapacityClick,
                remainingCapacityCandidate = remainingCapacityCandidate,
                installedOnFormatted = installedOnFormatted,
                onInstalledOnClick = onInstalledOnClick,
                installedOnCandidateFormatted = installedOnCandidateFormatted
            )
            // Details Actions
            DetailsActions(
                modifier = Modifier.padding(top = 8.dp),
                editMode = editMode,
                onEdit = onEdit,
                onClearData = onClearData,
                onCancel = onCancel,
                onSave = onSave
            )
        }
    }
}

@ExperimentalAnimationApi
@Composable
private fun DetailsActions(
    modifier: Modifier = Modifier,
    editMode: Boolean,
    // Callbacks
    onEdit: () -> Unit,
    onClearData: () -> Unit,
    onCancel: () -> Unit,
    onSave: () -> Unit
) {
    Box(modifier) {
        AnimatedVisibility(
            visible = editMode,
            enter = slideInHorizontally(initialOffsetX = { fullWidth ->
                -fullWidth
            }),
            exit = slideOutHorizontally(targetOffsetX = { fullWidth ->
                -fullWidth
            })
        ) {
            DetailsEditModeActions(
                onClearData = onClearData,
                onCancel = onCancel,
                onSave = onSave
            )
        }
        AnimatedVisibility(
            visible = !editMode,
            enter = slideInHorizontally(initialOffsetX = { fullWidth ->
                fullWidth
            }),
            exit = slideOutHorizontally(targetOffsetX = { fullWidth ->
                fullWidth
            })
        ) {
            DetailsPermanentActions(onEdit = onEdit)
        }
    }
}

@Composable
private fun DetailsPermanentActions(
    modifier: Modifier = Modifier,
    onEdit: () -> Unit,
) {
    Box(modifier.fillMaxWidth()) { // Non-edit mode
        TextButton(
            modifier = Modifier.align(Alignment.Center),
            onClick = onEdit,
            colors = ButtonDefaults.textButtonColors(
                contentColor = MaterialTheme.colors.onSurface
            )
        ) {
            Text(text = (stringResource(id = R.string.details_card_edit).uppercase()))
        }
    }
}

@Composable
private fun DetailsEditModeActions(
    modifier: Modifier = Modifier,
    onClearData: () -> Unit,
    onCancel: () -> Unit,
    onSave: () -> Unit
) {
    Row(modifier) { // Edit mode
        TextButton(
            onClick = onClearData,
            colors = ButtonDefaults.textButtonColors(
                contentColor = MaterialTheme.colors.onSurface
            )
        ) {
            Text(text = stringResource(id = R.string.details_card_clear_data).uppercase())
        }
        Spacer(Modifier.weight(1f))
        TextButton(
            onClick = onCancel,
            colors = ButtonDefaults.textButtonColors(
                contentColor = MaterialTheme.colors.onSurface
            )
        ) {
            Text(text = stringResource(id = R.string.details_card_cancel).uppercase())
        }
        TextButton(
            onClick = onSave
        ) {
            Text(text = stringResource(id = R.string.details_card_save).uppercase())
        }
    }
}