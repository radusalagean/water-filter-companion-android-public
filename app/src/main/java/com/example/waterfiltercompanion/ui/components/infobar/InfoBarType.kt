package com.example.waterfiltercompanion.ui.components.infobar

import androidx.compose.ui.graphics.Color
import com.example.waterfiltercompanion.ui.theme.ColorInfoBarErrorBg
import com.example.waterfiltercompanion.ui.theme.ColorInfoBarFg
import com.example.waterfiltercompanion.ui.theme.ColorInfoBarInfoBg
import com.example.waterfiltercompanion.ui.theme.ColorInfoBarWarnBg

enum class InfoBarType(
    val bgColor: Color,
    val fgColor: Color
) {
    INFO(bgColor = ColorInfoBarInfoBg, fgColor = ColorInfoBarFg),
    WARN(bgColor = ColorInfoBarWarnBg, fgColor = ColorInfoBarFg),
    ERROR(bgColor = ColorInfoBarErrorBg, fgColor = ColorInfoBarFg)
}