package better.text.protext.localdata.database.daos

import androidx.paging.PagingSource
import androidx.room.*
import better.text.protext.localdata.database.entities.CopiedText

@Dao
abstract class CopiedTextDao {
    @Query("SELECT * FROM copied_texts ORDER BY last_updated DESC")
    abstract fun getAll(): PagingSource<Int, CopiedText>

    @Query("SELECT * FROM copied_texts WHERE id = (:copiedTextId)")
    abstract suspend fun get(copiedTextId: Long): CopiedText

    @Insert
    abstract suspend fun addCopiedText(copiedText: CopiedText)

    @Query("DELETE FROM copied_texts WHERE id IN (:copiedTexts)")
    abstract suspend fun deleteCopiedTexts(copiedTexts: List<Long>): Int

    @Update
    abstract suspend fun updateCopiedText(copiedText: CopiedText): Int
}
