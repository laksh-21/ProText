package better.text.protext.localdata.database.utils

import androidx.room.TypeConverter
import java.text.SimpleDateFormat
import java.util.*

data class UpdateTime(
    val time: Long,
    val stringTime: String,
    val stringDate: String,
) {
    class TimeConverter {
        @TypeConverter
        fun longToTime(value: Long): UpdateTime {
            val dateFormatter = SimpleDateFormat("dd/MMM/yy", Locale.getDefault())
            val timeFormatter = SimpleDateFormat("HH:mm", Locale.getDefault())
            val date = Date(value)
            return UpdateTime(
                time = value,
                stringTime = timeFormatter.format(date),
                stringDate = dateFormatter.format(date)
            )
        }

        @TypeConverter
        fun timeToLong(value: UpdateTime) = value.time
    }
}
