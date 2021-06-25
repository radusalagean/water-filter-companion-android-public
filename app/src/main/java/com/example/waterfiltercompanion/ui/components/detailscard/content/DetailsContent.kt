package com.example.waterfiltercompanion.ui.components.detailscard.content

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.waterfiltercompanion.R
import com.example.waterfiltercompanion.ui.components.detailscard.content.item.DetailsContentItem
import com.example.waterfiltercompanion.ui.utils.stringResourceWithFallback
import com.example.waterfiltercompanion.ui.utils.stringWithFallback

@Composable
fun DetailsContent(
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
    installedOnCandidateFormatted: String?
) {

    @Composable
    fun Total(itemModifier: Modifier = Modifier) {
        DetailsContentItem(
            modifier = itemModifier,
            value = stringResourceWithFallback(
                R.string.details_card_total_format,
                totalCapacity?.toString()
            ),
            candidateValue = stringResourceWithFallback(
                R.string.details_card_total_format,
                totalCapacityCandidate
            ),
            label = stringResource(R.string.details_card_total_label),
            onClick = onTotalCapacityClick,
            editMode = editMode
        )
    }

    @Composable
    fun Remaining(itemModifier: Modifier = Modifier) {
        DetailsContentItem(
            modifier = itemModifier,
            value = stringResourceWithFallback(
                R.string.details_card_remaining_format,
                remainingCapacity?.toString()
            ),
            candidateValue = stringResourceWithFallback(
                R.string.details_card_remaining_format,
                remainingCapacityCandidate
            ),
            label = stringResource(R.string.details_card_remaining_label),
            onClick = onRemainingCapacityClick,
            editMode = editMode
        )
    }

    @Composable
    fun InstalledOn(itemModifier: Modifier = Modifier) {
        DetailsContentItem(
            modifier = itemModifier,
            value = stringWithFallback(installedOnFormatted),
            candidateValue = stringWithFallback(installedOnCandidateFormatted),
            label = stringResource(R.string.details_card_installed_on_label),
            onClick = onInstalledOnClick,
            editMode = editMode
        )
    }

    @Composable
    fun DefaultLayout(modifier: Modifier = Modifier) {
        Row(
            modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)) {
            val itemModifier = Modifier.weight(1f)
            Total(itemModifier)
            VerticalDivider()
            Remaining(itemModifier)
            VerticalDivider()
            InstalledOn(itemModifier)
        }
    }

    @Composable
    fun LandscapeLayout(modifier: Modifier = Modifier) {
        Column(modifier) {
            Row(Modifier.height(IntrinsicSize.Min)) {
                val itemModifier = Modifier.weight(1f)
                Total(itemModifier)
                VerticalDivider()
                Remaining(itemModifier)
            }
            InstalledOn(Modifier.align(Alignment.CenterHorizontally))
        }
    }

    val configuration = LocalConfiguration.current
    when(configuration.orientation) {
        Configuration.ORIENTATION_LANDSCAPE -> LandscapeLayout(modifier)
        else -> DefaultLayout(modifier)
    }

}

@Composable
fun VerticalDivider() {
    Divider(
        Modifier
            .fillMaxHeight()
            .width(1.dp))
}