package com.example.waterfiltercompanion.ui.utils

import androidx.annotation.PluralsRes
import androidx.annotation.StringRes
import androidx.compose.foundation.border
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
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

@Composable
fun stringResourceWithFallback(
    @StringRes stringResFormat: Int,
    argument: Any?
): String {
    return if (argument != null)
        stringResource(stringResFormat, argument)
    else stringResource(R.string.not_available)
}

@Composable
fun stringWithFallback(string: String?): String {
    return string ?: stringResource(R.string.not_available)
}

fun Modifier.debugBorder(color: Color = Color.Red): Modifier = this.border(
    width = 1.dp,
    color = color
)