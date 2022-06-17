package better.text.protext.repository.copiedTexts

import androidx.paging.PagingSource
import better.text.protext.localdata.database.daos.CopiedTextDao
import better.text.protext.localdata.database.entities.CopiedText
import javax.inject.Inject

class CopiedTextRepoImpl @Inject constructor(
    private val copiedTextDao: CopiedTextDao
) : CopiedTextRepo {
    override fun getCopiedTexts(): PagingSource<Int, CopiedText> {
        return copiedTextDao.getAll()
    }

    override suspend fun addCopiedText(copiedText: CopiedText) {
        copiedTextDao.addCopiedText(copiedText)
    }

    override suspend fun deleteCopiedText(copiedTexts: List<Long>) {
        copiedTextDao.deleteCopiedTexts(copiedTexts)
    }

    override suspend fun editCopiedText(copiedText: CopiedText) {
        copiedTextDao.updateCopiedText(copiedText)
    }

    override suspend fun getCopiedText(copiedTextId: Long): CopiedText {
        return copiedTextDao.get(copiedTextId)
    }
}
