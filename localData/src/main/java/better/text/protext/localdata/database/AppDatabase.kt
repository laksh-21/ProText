package better.text.protext.localdata.database

import androidx.room.Database
import better.text.protext.localdata.database.daos.BookmarkDao
import better.text.protext.localdata.database.daos.BookmarkFolderDao
import better.text.protext.localdata.database.daos.CopiedTextDao
import better.text.protext.localdata.database.entities.Bookmark
import better.text.protext.localdata.database.entities.BookmarkFolder
import better.text.protext.localdata.database.entities.CopiedText

@Database(
    entities = [
        Bookmark::class,
        BookmarkFolder::class,
        CopiedText::class
    ],
    version = 1
)
abstract class AppDatabase {
    abstract fun bookmarkDao(): BookmarkDao
    abstract fun bookmarkFolderDao(): BookmarkFolderDao
    abstract fun copiedTextDao(): CopiedTextDao
}
