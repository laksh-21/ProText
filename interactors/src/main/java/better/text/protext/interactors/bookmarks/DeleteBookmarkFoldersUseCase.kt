package better.text.protext.interactors.bookmarks

import better.text.protext.base.di.IODispatcher
import better.text.protext.interactors.Interactors
import better.text.protext.repository.bookmarks.BookmarkFolderRepo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DeleteBookmarkFoldersUseCase @Inject constructor(
    @IODispatcher private val coroutineDispatcher: CoroutineDispatcher,
    private val bookmarkFolderRepo: BookmarkFolderRepo
) : Interactors.SuspendUseCase<DeleteBookmarkFoldersUseCase.Params, Unit>() {
    data class Params(
        val bookmarkFolders: List<Long>
    )

    override suspend fun doWork(params: Params): Unit = withContext(coroutineDispatcher) {
        bookmarkFolderRepo.deleteFolder(params.bookmarkFolders)
    }
}
