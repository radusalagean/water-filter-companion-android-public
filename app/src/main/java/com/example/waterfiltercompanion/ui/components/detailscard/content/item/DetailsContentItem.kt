package com.example.waterfiltercompanion.ui.components.detailscard.content.item

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.sp

@Composable
fun DetailsContentItem(
    modifier: Modifier = Modifier,
    value: String,
    label: String,
    editMode: Boolean,
    onClick: () -> Unit
) {
    Column(modifier) {
        val itemModifier = Modifier.align(Alignment.CenterHorizontally)
        DetailsContentTextItemValue(
            modifier = itemModifier.clickable(
                enabled = editMode,
                role = Role.Button,
                onClick = onClick
            ),
            value = value
        )
        DetailsContentItemLabel(
            modifier = itemModifier,
            label = label
        )
    }
}

@Composable
private fun DetailsContentTextItemValue(
    modifier: Modifier = Modifier,
    value: String
) {
    Text(
        modifier = modifier,
        text = value,
        style = MaterialTheme.typography.h4
    )
}

@Composable
private fun DetailsContentItemLabel(
    modifier: Modifier = Modifier,
    label: String
) {
    Text(
        modifier = modifier,
        text = label.uppercase(),
        style = MaterialTheme.typography.overline,
        fontSize = 12.sp
    )
}