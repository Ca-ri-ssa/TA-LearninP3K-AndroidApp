package com.carissac.learninp3k.view.utils

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
fun formatDate(timestamp: String): String {
    val inputFormat = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.getDefault())
    val date = ZonedDateTime.parse(timestamp)
    return date.format(inputFormat)
}