package com.carissac.learninp3k.view.utils

import android.annotation.SuppressLint
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import java.time.OffsetDateTime
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

@RequiresApi(Build.VERSION_CODES.O)
fun formatDateTime(timeStamp: String): String {
    val parsedDateTime = OffsetDateTime.parse(timeStamp)
    val localDateTime = parsedDateTime.toLocalDateTime()
    val formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy - HH:mm", Locale("id", "ID"))
    return localDateTime.format(formatter)
}

@SuppressLint("DefaultLocale")
fun formatTimer(seconds: Long): String {
    val minutes = seconds / 60
    val remainingSeconds = seconds % 60
    return String.format("%02d:%02d", minutes, remainingSeconds)
}