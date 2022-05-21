package better.text.protext.localdata.database.daos

import androidx.room.*
import better.text.protext.localdata.database.entities.CopiedText
import kotlinx.coroutines.flow.Flow

@Dao
abstract class CopiedTextDao {
    @Query("SELECT * FROM copied_texts")
    abstract suspend fun getAll(): Flow<List<CopiedText>>

    @Insert
    abstract suspend fun addCopiedText(copiedText: CopiedText): Int

    @Delete
    abstract suspend fun deleteCopiedText(copiedText: CopiedText): Int

    @Update
    abstract suspend fun updateCopiedText(copiedText: CopiedText): Int
}
