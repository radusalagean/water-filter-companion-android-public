package com.example.waterfiltercompanion.ui.components.detailscard

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.waterfiltercompanion.R
import com.example.waterfiltercompanion.ui.components.detailscard.content.item.DetailsContentItem

@Composable
fun DetailsCard(
    modifier: Modifier = Modifier,
    editState: Boolean
) {
    Card(modifier) {
        Column {
            // Details Content
            Row(Modifier.fillMaxWidth().height(IntrinsicSize.Min)) {
                val itemModifier = Modifier.weight(1f)
                DetailsContentItem(
                    modifier = itemModifier,
                    value = "350 L",
                    label = stringResource(R.string.details_card_total_label)
                )
                Divider(Modifier.fillMaxHeight().width(1.dp))
                DetailsContentItem(
                    modifier = itemModifier,
                    value = "220 L",
                    label = stringResource(R.string.details_card_remaining_label)
                )
                Divider(Modifier.fillMaxHeight().width(1.dp))
                DetailsContentItem(
                    modifier = itemModifier,
                    value = "Feb 8",
                    label = stringResource(R.string.details_card_installed_on_label)
                )
            }
            // Details Actions
            if (!editState) {
                Box(Modifier.fillMaxWidth()) { // Non-edit state
                    TextButton(
                        modifier = Modifier.align(Alignment.Center),
                        onClick = { Log.d("DetailsCard", "onClick Edit") },
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = MaterialTheme.colors.onSurface
                        )
                    ) {
                        Text(text = (stringResource(id = R.string.details_card_edit).uppercase()))
                    }
                }
            } else {
                Row { // Edit State
                    TextButton(
                        onClick = {},
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = MaterialTheme.colors.onSurface
                        )
                    ) {
                        Text(text = stringResource(id = R.string.details_card_clear_data).uppercase())
                    }
                    Spacer(Modifier.weight(1f))
                    TextButton(
                        onClick = {},
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = MaterialTheme.colors.onSurface
                        )
                    ) {
                        Text(text = stringResource(id = R.string.details_card_cancel).uppercase())
                    }
                    TextButton(
                        onClick = {}
                    ) {
                        Text(text = stringResource(id = R.string.details_card_save).uppercase())
                    }
                }
            }
        }
    }
}

