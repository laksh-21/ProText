package better.text.protext.base.utils

import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import java.util.*

object TimeHelpers {
    private const val dateString = "dd MMM"
    private const val dateStringWithYear = "dd MMM yy"
    private const val minutesDivider = 1000L * 60L
    private const val hoursDivider = minutesDivider * 60L

    fun getTimeString(time: Long): String {
        val updateTime = Calendar.getInstance().apply {
            setTime(Date(time))
        }
        val today = Calendar.getInstance()
        val aMinuteAgo = Calendar.getInstance().apply {
            add(Calendar.MINUTE, -1)
        }
        val anHourAgo = Calendar.getInstance().apply {
            add(Calendar.HOUR, -1)
        }
        val aDayAgo = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_MONTH, -1)
        }
        return when (time) {
            in (aMinuteAgo.time.time until today.time.time) -> "now" // now
            in (anHourAgo.time.time until aMinuteAgo.time.time) -> {
                val minutes = (today.time.time - time) / minutesDivider
                "$minutes min"
            } // minutes
            in (aDayAgo.time.time until anHourAgo.time.time) -> {
                val hours = (today.time.time - time) / hoursDivider
                "$hours hrs"
            } // hours
            else -> {
                val formatter = if (today.get(Calendar.YEAR) == updateTime.get(Calendar.YEAR)) {
                    SimpleDateFormat(dateString, Locale.getDefault())
                } else {
                    SimpleDateFormat(dateStringWithYear, Locale.getDefault())
                }
                formatter.format(updateTime.time)
            } // date
        }
    }
}
