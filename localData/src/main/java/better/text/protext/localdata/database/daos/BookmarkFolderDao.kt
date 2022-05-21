package better.text.protext.localdata.database.daos

import androidx.room.*
import better.text.protext.localdata.database.entities.BookmarkFolder
import kotlinx.coroutines.flow.Flow

@Dao
abstract class BookmarkFolderDao {
    @Query("SELECT * FROM bookmark_folder")
    abstract suspend fun getAll(): Flow<List<BookmarkFolder>>

    @Insert
    abstract suspend fun addNewFolder(bookmarkFolder: BookmarkFolder): Int

    @Delete
    abstract suspend fun deleteFolder(bookmarkFolder: BookmarkFolder): Int

    @Update
    abstract suspend fun editFolder(bookmarkFolder: BookmarkFolder): Int
}
