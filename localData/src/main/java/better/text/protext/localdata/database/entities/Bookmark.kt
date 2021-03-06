package better.text.protext.localdata.database.entities

import androidx.room.*
import java.util.*

@Entity(
    tableName = "bookmark",
    foreignKeys = [
        ForeignKey(
            entity = BookmarkFolder::class,
            parentColumns = ["id"],
            childColumns = ["bookmark_folder_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["bookmark_folder_id"])
    ]
)
data class Bookmark(
    @PrimaryKey(autoGenerate = true) val id: Long,
    @ColumnInfo(name = "bookmark_title") val bookmarkTitle: String,
    @ColumnInfo(name = "bookmark_url") val bookmarkUrl: String,
    @ColumnInfo(name = "bookmark_folder_id") val bookmarkFolderId: Long,
    @ColumnInfo(name = "last_updated") val lastUpdated: Long = Date().time,
)
