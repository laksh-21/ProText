package better.text.protext.localdata.database.daos

import androidx.room.*
import better.text.protext.localdata.database.entities.Bookmark
import kotlinx.coroutines.flow.Flow

@Dao
abstract class BookmarkDao {
    @Query("SELECT * FROM bookmark")
    abstract suspend fun getAll(): Flow<List<Bookmark>>

    @Insert
    abstract suspend fun addBookmark(bookmark: Bookmark): Int

    @Delete
    abstract suspend fun deleteBookmark(bookmark: Bookmark): Int

    @Update
    abstract suspend fun editBookmark(bookmark: Bookmark): Int
}
