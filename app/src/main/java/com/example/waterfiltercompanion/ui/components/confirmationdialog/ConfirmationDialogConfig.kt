package com.example.waterfiltercompanion.ui.components.confirmationdialog

import androidx.annotation.StringRes

data class ConfirmationDialogConfig(
    @StringRes val titleStringRes: Int,
    val onConfirm: () -> Unit,
    val onCancel: () -> Unit
)
