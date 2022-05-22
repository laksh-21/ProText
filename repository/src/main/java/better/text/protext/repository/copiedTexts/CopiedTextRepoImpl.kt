package better.text.protext.repository.copiedTexts

import androidx.paging.PagingSource
import better.text.protext.localdata.database.daos.CopiedTextDao
import better.text.protext.localdata.database.entities.CopiedText
import javax.inject.Inject

class CopiedTextRepoImpl @Inject constructor(
    private val copiedTextDao: CopiedTextDao
) : CopiedTextRepo {
    override suspend fun getCopiedTexts(): PagingSource<Int, CopiedText> {
        return copiedTextDao.getAll()
    }

    override suspend fun addCopiedText(copiedText: CopiedText) {
        copiedTextDao.addCopiedText(copiedText)
    }

    override suspend fun deleteCopiedText(copiedText: CopiedText) {
        copiedTextDao.deleteCopiedText(copiedText)
    }

    override suspend fun editCopiedText(copiedText: CopiedText) {
        copiedTextDao.updateCopiedText(copiedText)
    }
}
