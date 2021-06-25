package com.example.waterfiltercompanion.ui.components.capacityinputdialog

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.waterfiltercompanion.R

@Composable
fun CapacityInputDialog(modifier: Modifier = Modifier, config: CapacityInputDialogConfig?) {
    if (config == null) return
    var input by rememberSaveable { mutableStateOf(config.initialInput ?: "") }
    AlertDialog(
        modifier = modifier,
        onDismissRequest = config.onDismiss,
        text = {
            Column {
                CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.high) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(config.titleStringRes),
                        style = MaterialTheme.typography.h6,
                        textAlign = TextAlign.Center
                    )
                }
                OutlinedTextField(
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .fillMaxWidth(),
                    value = input,
                    onValueChange = {
                        if (isInputValid(it)) {
                            input = it
                        }
                    },
                    singleLine = true,
                    textStyle = MaterialTheme.typography.h4.copy(textAlign = TextAlign.Center),
                    keyboardOptions = KeyboardOptions(
                        autoCorrect = false,
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = { onSubmit(input, config) }
                    )
                )
            }
        },
        buttons = {
            Row(
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 12.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                val buttonModifier = Modifier.align(Alignment.CenterVertically)
                TextButton(
                    modifier = buttonModifier,
                    onClick = config.onDismiss,
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colors.onSurface
                    )
                ) {
                    Text(text = stringResource(R.string.capacity_input_dialog_cancel).uppercase())
                }
                TextButton(
                    modifier = buttonModifier,
                    onClick = { onSubmit(input, config) }
                ) {
                    Text(text = stringResource(R.string.capacity_input_dialog_submit).uppercase())
                }
            }
        }
    )
}

private fun onSubmit(input: String, config: CapacityInputDialogConfig) {
    if (input.isNotEmpty()) {
        config.onSubmit(input)
    }
}

/**
 * Rules:
 * - Max 3 digits
 * - Ignore any non-digit character
 * - Can be empty
 */
private fun isInputValid(input: String): Boolean {
    return input.isEmpty() ||
            (input.length <= 3 && input.matches(Regex("[0-9]*")))
}