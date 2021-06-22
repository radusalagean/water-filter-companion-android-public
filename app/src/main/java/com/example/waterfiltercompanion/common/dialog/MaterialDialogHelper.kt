package com.example.waterfiltercompanion.common.dialog

import android.content.Context
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.datetime.datePicker
import java.util.*

class MaterialDialogHelper(
    private val context: Context
) {

    fun requestDateAsTimestamp(currentDateTimestamp: Long?, cb: (Long) -> Unit) {
        val currentDateCalendar = currentDateTimestamp?.let {
            Calendar.getInstance().apply {
                timeInMillis = currentDateTimestamp
            }
        }
        MaterialDialog(context).show {
            datePicker(
                maxDate = Calendar.getInstance(),
                currentDate = currentDateCalendar
            ) { dialog, datetime ->
                cb(datetime.timeInMillis)
            }
        }
    }
}