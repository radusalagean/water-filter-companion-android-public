package com.example.waterfiltercompanion.ui.components.confirmationdialog

import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.waterfiltercompanion.R

@Composable
fun ConfirmationDialog(modifier: Modifier = Modifier, config: ConfirmationDialogConfig?) {
    if (config == null) return
    AlertDialog(
        modifier = modifier,
        onDismissRequest = config.onCancel,
        confirmButton = {
            TextButton(onClick = config.onConfirm) {
                Text(stringResource(R.string.confirmation_dialog_confirm))
            }
        },
        dismissButton = {
            TextButton(
                onClick = config.onCancel,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colors.onSurface
                )
            ) {
                Text(stringResource(R.string.confirmation_dialog_cancel))
            }
        },
        title = {
            Text(
                text = stringResource(R.string.clear_data_confirmation_dialog_title),
                style = MaterialTheme.typography.h6
            )
        }
    )
}