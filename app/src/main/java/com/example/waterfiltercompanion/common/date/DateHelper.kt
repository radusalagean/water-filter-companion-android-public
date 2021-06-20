package com.example.waterfiltercompanion.common.date

import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class DateHelper {

    private val simpleDateFormat = SimpleDateFormat(
        PATTERN,
        Locale.US
    )

    fun getFormattedDate(timestamp: Long): String? = simpleDateFormat.format(Date(timestamp))

    fun getDaysSince(timestamp: Long): Int? {
        val currentTime = System.currentTimeMillis()
        val diff = currentTime - timestamp
        return if (diff <= 0) null else {
            TimeUnit.MILLISECONDS.toDays(diff).toInt()
        }
    }

    companion object {
        const val PATTERN = "MMM d"
    }
}