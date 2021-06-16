package com.example.waterfiltercompanion.ui.utils

import androidx.annotation.PluralsRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.example.waterfiltercompanion.R

@Composable
fun quantityStringResource(
    @PluralsRes id: Int,
    quantity: Int
): String {
    return LocalContext.current.resources.getQuantityString(id, quantity, quantity)
}

@Composable
fun quantityStringResourceWithFallback(
    @PluralsRes id: Int,
    quantity: Int?
): String {
    return if (quantity != null)
        quantityStringResource(id, quantity)
    else stringResource(R.string.not_available)
}