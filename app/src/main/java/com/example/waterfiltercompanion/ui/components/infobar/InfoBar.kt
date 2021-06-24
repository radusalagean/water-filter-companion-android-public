package com.example.waterfiltercompanion.ui.components.infobar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import java.util.concurrent.TimeUnit

const val SHOW_DELAY = 200L

@ExperimentalAnimationApi
@Composable
fun InfoBar(
    modifier: Modifier = Modifier,
    offeredMessage: InfoBarMessage?,
    onMessageTimeout: () -> Unit
) {
    val displayedMessage: MutableState<InfoBarMessage?> = remember { mutableStateOf(null) }
    val isShown: MutableState<Boolean> = remember { mutableStateOf(false) }
    offeredMessage?.let { offeredMessage ->
        LaunchedEffect(offeredMessage) {
            showMessage(
                offeredMessage = offeredMessage,
                displayedMessage = displayedMessage,
                isShown = isShown,
                onMessageTimeout = onMessageTimeout
            )
        }
    }
    AnimatedVisibility(
        visible = isShown.value,
        enter = slideInVertically(
            initialOffsetY = { fullHeight -> -fullHeight },
            animationSpec = tween(
                durationMillis = 150,
                easing = LinearOutSlowInEasing
            )
        ),
        exit = slideOutVertically(
            targetOffsetY = { fullHeight -> -fullHeight },
            animationSpec = tween(
                durationMillis = 250,
                easing = FastOutLinearInEasing
            )
        )
    ) {
        displayedMessage.value?.let { message ->
            Surface(
                modifier = modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                elevation = 8.dp,
                color = message.type.bgColor
            ) {
                Text(
                    modifier = Modifier.padding(
                        horizontal = 8.dp,
                        vertical = 4.dp
                    ),
                    text = stringResourceForMessage(message),
                    color = message.type.fgColor,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    maxLines = 5,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

private suspend fun showMessage(
    offeredMessage: InfoBarMessage,
    displayedMessage: MutableState<InfoBarMessage?>,
    isShown: MutableState<Boolean>,
    onMessageTimeout: () -> Unit
) {
    isShown.value = false
    delay(SHOW_DELAY)
    displayedMessage.value = offeredMessage
    isShown.value = true
    delay(TimeUnit.SECONDS.toMillis(offeredMessage.displayTimeSeconds))
    isShown.value = false
    onMessageTimeout()
}

@Composable
private fun stringResourceForMessage(message: InfoBarMessage): String {
    return message.args?.let {
        stringResource(message.textStringRes, *message.args.toTypedArray())
    } ?: stringResource(message.textStringRes)
}