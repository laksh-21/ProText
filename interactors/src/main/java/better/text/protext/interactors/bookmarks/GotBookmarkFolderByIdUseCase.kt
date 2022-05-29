package better.text.protext.interactors.bookmarks

import better.text.protext.base.di.IODispatcher
import better.text.protext.interactors.Interactors
import better.text.protext.localdata.database.daos.BookmarkFolderDao
import better.text.protext.localdata.database.entities.BookmarkFolder
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GotBookmarkFolderByIdUseCase @Inject constructor(
    private val bookmarkFolderDao: BookmarkFolderDao,
    @IODispatcher val coroutineDispatcher: CoroutineDispatcher
) : Interactors.SuspendUseCase<GotBookmarkFolderByIdUseCase.Params, BookmarkFolder>() {
    data class Params(
        val folderId: Long
    )

    override suspend fun doWork(params: Params): BookmarkFolder = withContext(coroutineDispatcher) {
        bookmarkFolderDao.get(params.folderId)
    }
}
