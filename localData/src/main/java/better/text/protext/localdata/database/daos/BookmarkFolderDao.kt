package better.text.protext.localdata.database.daos

import androidx.paging.PagingSource
import androidx.room.*
import better.text.protext.localdata.database.entities.BookmarkFolder

@Dao
abstract class BookmarkFolderDao {
    @Query("SELECT * FROM bookmark_folder ORDER BY last_updated DESC")
    abstract fun getAll(): PagingSource<Int, BookmarkFolder>

    @Insert
    abstract suspend fun addNewFolder(bookmarkFolder: BookmarkFolder): Int

    @Delete
    abstract suspend fun deleteFolder(bookmarkFolder: BookmarkFolder): Int

    @Update
    abstract suspend fun editFolder(bookmarkFolder: BookmarkFolder): Int
}
