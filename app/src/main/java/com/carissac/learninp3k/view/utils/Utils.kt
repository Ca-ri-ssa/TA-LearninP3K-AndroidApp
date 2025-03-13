package com.carissac.learninp3k.view.utils

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
fun formatDate(timestamp: String): String {
    val localeID = Locale("id", "ID")
    val inputFormat = DateTimeFormatter.ofPattern("dd MMMM yyyy", localeID)
    val date = ZonedDateTime.parse(timestamp)
    return date.format(inputFormat)
}