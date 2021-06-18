package com.example.waterfiltercompanion.ui.components.detailscard.content

import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.waterfiltercompanion.R
import com.example.waterfiltercompanion.ui.components.detailscard.content.item.DetailsContentItem
import com.example.waterfiltercompanion.ui.utils.stringResourceWithFallback
import com.example.waterfiltercompanion.ui.utils.stringWithFallback

@Composable
fun DetailsContent(
    modifier: Modifier = Modifier,
    totalCapacity: Int?,
    remainingCapacity: Int?,
    installedOnFormatted: String?
) {
    Row(
        modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)) {
        val itemModifier = Modifier.weight(1f)
        DetailsContentItem(
            modifier = itemModifier,
            value = stringResourceWithFallback(R.string.details_card_total_format, totalCapacity),
            label = stringResource(R.string.details_card_total_label)
        )
        Divider(
            Modifier
                .fillMaxHeight()
                .width(1.dp))
        DetailsContentItem(
            modifier = itemModifier,
            value = stringResourceWithFallback(R.string.details_card_remaining_format, remainingCapacity),
            label = stringResource(R.string.details_card_remaining_label)
        )
        Divider(
            Modifier
                .fillMaxHeight()
                .width(1.dp))
        DetailsContentItem(
            modifier = itemModifier,
            value = stringWithFallback(installedOnFormatted),
            label = stringResource(R.string.details_card_installed_on_label)
        )
    }
}