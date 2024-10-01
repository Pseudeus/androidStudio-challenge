package com.example.challenge

import android.app.DatePickerDialog
import android.content.Context
import android.icu.util.Calendar
import android.widget.EditText
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

class CustomDatePicker {

    fun showDatePickerDialog(context: Context, etBirthDate: EditText) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            context,
            { _, selectedYear, selectedMonth, selectedDay ->
                val formattedDate = String.format("%02d/%02d/%d", selectedDay, selectedMonth + 1, selectedYear)
                etBirthDate.setText(formattedDate)
            },
            year, month, day
        )

        // Setting the min and max dates
        val minDate = Calendar.getInstance().apply {
            set(1900, 0, 1) // Setting minimum date to January 1, 1900
        }.timeInMillis

        val maxDate = Calendar.getInstance().apply {
            add(Calendar.YEAR, 100) // Setting maximum date to 100 years in the future
        }.timeInMillis

        datePickerDialog.datePicker.minDate = minDate
        datePickerDialog.datePicker.maxDate = maxDate

        datePickerDialog.show()
    }

    fun getCurrentUtcTime(): String {
        val utcTime = Instant.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
            .withZone(ZoneOffset.UTC)

        return formatter.format(utcTime)
    }
}