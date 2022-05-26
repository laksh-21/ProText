package better.text.protext.localdata.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import better.text.protext.localdata.database.utils.TimeConverter
import better.text.protext.localdata.database.utils.UpdateTime

@Entity(
    tableName = "copied_texts"
)
data class CopiedText(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "text") val text: String,
    @TypeConverters(TimeConverter::class)
    @ColumnInfo(name = "last_updated") val lastUpdated: UpdateTime
)
