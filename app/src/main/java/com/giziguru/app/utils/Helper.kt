package com.giziguru.app.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object Helper {

    object Utils {
        private val dateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault())

        fun formatDateTime(calendar: Calendar): String {
            return dateFormat.format(calendar.time)
        }

        private val simpleDateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        fun simpleDateFormat(date: Long): String {
            return simpleDateFormat.format(date)
        }
    }
}