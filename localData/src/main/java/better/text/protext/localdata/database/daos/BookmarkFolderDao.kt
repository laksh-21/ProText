package better.text.protext.localdata.database.daos

import androidx.paging.PagingSource
import androidx.room.*
import better.text.protext.localdata.database.entities.BookmarkFolder

@Dao
abstract class BookmarkFolderDao {
    @Query("SELECT * FROM bookmark_folder ORDER BY last_updated DESC")
    abstract fun getAll(): PagingSource<Int, BookmarkFolder>

    @Query("SELECT * FROM bookmark_folder WHERE id = :id")
    abstract fun get(id: Long): BookmarkFolder

    @Insert
    abstract suspend fun addNewFolder(bookmarkFolder: BookmarkFolder)

    @Query("DELETE FROM bookmark_folder WHERE id IN (:bookmarkFolders)")
    abstract suspend fun deleteFolder(bookmarkFolders: List<Long>): Int

    @Update
    abstract suspend fun editFolder(bookmarkFolder: BookmarkFolder): Int
}
