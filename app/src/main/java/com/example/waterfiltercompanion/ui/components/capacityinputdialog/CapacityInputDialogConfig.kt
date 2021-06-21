package com.example.waterfiltercompanion.ui.components.capacityinputdialog

import androidx.annotation.StringRes

data class CapacityInputDialogConfig(
    @StringRes val titleStringRes: Int,
    val initialInput: String?,
    val onSubmit: (String) -> Unit,
    val onDismiss: () -> Unit
)
