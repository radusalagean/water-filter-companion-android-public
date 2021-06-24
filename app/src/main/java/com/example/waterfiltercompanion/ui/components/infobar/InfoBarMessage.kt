package com.example.waterfiltercompanion.ui.components.infobar

import androidx.annotation.StringRes

data class InfoBarMessage(
    val type: InfoBarType,
    @StringRes val textStringRes: Int,
    val args: List<Any>? = null,
    val displayTimeSeconds: Long = 3L,
    val creationTime: Long = System.currentTimeMillis()
)
