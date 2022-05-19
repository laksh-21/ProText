package better.text.protext.localdata.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "bookmark_folder"
)
data class BookmarkFolder(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "folder_name", defaultValue = "") val folderName: String,
    @ColumnInfo(name = "folder_color", defaultValue = "0xff00000") val folderColor: String // TODO: Add type converter
)
