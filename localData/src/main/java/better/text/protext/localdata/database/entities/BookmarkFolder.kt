package better.text.protext.localdata.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import better.text.protext.localdata.database.utils.TimeConverter
import better.text.protext.localdata.database.utils.UpdateTime

@Entity(
    tableName = "bookmark_folder"
)
data class BookmarkFolder(
    @PrimaryKey(autoGenerate = true) val id: Long,
    @ColumnInfo(name = "folder_name", defaultValue = "") val folderName: String,
    @ColumnInfo(name = "folder_color", defaultValue = "#ffffff") val folderColor: String,
    @TypeConverters(TimeConverter::class)
    @ColumnInfo(name = "last_updated") val lastUpdated: UpdateTime
)
