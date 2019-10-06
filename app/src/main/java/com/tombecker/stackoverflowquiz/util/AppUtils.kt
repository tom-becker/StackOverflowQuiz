package com.tombecker.stackoverflowquiz.util

import java.text.SimpleDateFormat
import java.util.*


class AppUtils {

    companion object {
        fun getDateFromMillis(millis: Int?, dateFormat: String): String {
            val formatter = SimpleDateFormat(dateFormat, Locale.US)

            millis?.let {
                val date = Date(it.toLong() * 1000L)
                return formatter.format(date)
            }

            return ""
        }
    }

}