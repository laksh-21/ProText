package better.text.protext.localdata.database.daos

import androidx.paging.PagingSource
import androidx.room.*
import better.text.protext.localdata.database.entities.Bookmark

@Dao
abstract class BookmarkDao {
    @Query("SELECT * FROM bookmark ORDER BY last_updated DESC")
    abstract suspend fun getAll(): PagingSource<Int, Bookmark>

    @Query("SELECT * FROM bookmark WHERE bookmark_folder_id = :folderId ORDER BY last_updated DESC")
    abstract suspend fun getAllFromFolder(folderId: Long): PagingSource<Int, Bookmark>

    @Insert
    abstract suspend fun addBookmark(bookmark: Bookmark): Int

    @Delete
    abstract suspend fun deleteBookmark(bookmark: Bookmark): Int

    @Update
    abstract suspend fun editBookmark(bookmark: Bookmark): Int
}
