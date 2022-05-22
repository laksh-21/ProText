package better.text.protext.repository.copiedTexts

import androidx.paging.PagingSource
import better.text.protext.localdata.database.entities.CopiedText

interface CopiedTextRepo {
    fun getCopiedTexts(): PagingSource<Int, CopiedText>
    suspend fun addCopiedText(copiedText: CopiedText)
    suspend fun deleteCopiedText(copiedText: CopiedText)
    suspend fun editCopiedText(copiedText: CopiedText)
}
