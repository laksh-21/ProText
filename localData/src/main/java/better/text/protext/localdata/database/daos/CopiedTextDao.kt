package better.text.protext.localdata.database.daos

import androidx.paging.PagingSource
import androidx.room.*
import better.text.protext.localdata.database.entities.CopiedText

@Dao
abstract class CopiedTextDao {
    @Query("SELECT * FROM copied_texts ORDER BY last_updated DESC")
    abstract fun getAll(): PagingSource<Int, CopiedText>

    @Insert
    abstract suspend fun addCopiedText(copiedText: CopiedText): Int

    @Delete
    abstract suspend fun deleteCopiedText(copiedText: CopiedText): Int

    @Update
    abstract suspend fun updateCopiedText(copiedText: CopiedText): Int
}
