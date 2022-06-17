package better.text.protext.repository.copiedTexts

import androidx.paging.PagingSource
import better.text.protext.localdata.database.entities.CopiedText

interface CopiedTextRepo {
    fun getCopiedTexts(): PagingSource<Int, CopiedText>
    suspend fun addCopiedText(copiedText: CopiedText)
    suspend fun deleteCopiedText(copiedTexts: List<Long>)
    suspend fun editCopiedText(copiedText: CopiedText)
    suspend fun getCopiedText(copiedTextId: Long): CopiedText
}
