package better.text.protext.localdata.database.entities

import androidx.room.*
import better.text.protext.localdata.database.utils.UpdateTime

@Entity(
    tableName = "bookmark",
    foreignKeys = [
        ForeignKey(
            entity = BookmarkFolder::class,
            parentColumns = ["id"],
            childColumns = ["bookmark_folder_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.NO_ACTION
        )
    ]
)
data class Bookmark(
    @PrimaryKey(autoGenerate = true) val id: Long,
    @ColumnInfo(name = "bookmark_title") val bookmarkTitle: String,
    @ColumnInfo(name = "bookmark_url") val bookmarkUrl: String,
    @ColumnInfo(name = "bookmark_folder_id") val bookmarkFolderId: Int,
    @TypeConverters(UpdateTime.TimeConverter::class)
    @ColumnInfo(name = "last_updated") val lastUpdated: UpdateTime,
)
