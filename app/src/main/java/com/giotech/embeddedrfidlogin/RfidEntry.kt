package com.giotech.embeddedrfidlogin

import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

data class RfidEntry(
    @SerializedName("Name") val name: String,
    @SerializedName("UID") val uid: String,
    @SerializedName("Date") val date: String,
    @SerializedName("Time In") val timeIn: String
) {
    fun timestamp(): Long {
        return try {
            // Parse Date (ISO 8601)
            val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            dateFormat.timeZone = TimeZone.getTimeZone("UTC")
            val dateMillis = dateFormat.parse(date)?.time ?: 0L

            // Parse Time In (Google Sheets time-only format)
            // "1899-12-30T08:17:39.000Z"
            val cal = Calendar.getInstance()
            val timeDate = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
                .apply { timeZone = TimeZone.getTimeZone("UTC") }
                .parse(timeIn)
            cal.time = timeDate ?: Date(0)
            val timeMillis = cal.get(Calendar.HOUR_OF_DAY) * 3600_000 +
                    cal.get(Calendar.MINUTE) * 60_000 +
                    cal.get(Calendar.SECOND) * 1000

            dateMillis + timeMillis
        } catch (e: Exception) {
            e.printStackTrace()
            0L
        }
    }


}