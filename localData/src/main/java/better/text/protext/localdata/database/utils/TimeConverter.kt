package better.text.protext.localdata.database.utils

import androidx.room.TypeConverter
import java.text.SimpleDateFormat
import java.util.*

class TimeConverter {
    @TypeConverter
    fun toUpdateTime(value: Long): UpdateTime {
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
    fun fromUpdateTime(value: UpdateTime): Long = value.time
}
