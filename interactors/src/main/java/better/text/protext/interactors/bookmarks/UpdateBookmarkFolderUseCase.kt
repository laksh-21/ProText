package better.text.protext.interactors.bookmarks

import better.text.protext.base.di.IODispatcher
import better.text.protext.interactors.Interactors
import better.text.protext.localdata.database.entities.BookmarkFolder
import better.text.protext.repository.bookmarks.BookmarkFolderRepo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UpdateBookmarkFolderUseCase @Inject constructor(
    @IODispatcher private val coroutineDispatcher: CoroutineDispatcher,
    private val bookmarkFolderRepo: BookmarkFolderRepo
) : Interactors.SuspendUseCase<UpdateBookmarkFolderUseCase.Params, Unit>() {
    data class Params(
        val bookmarkFolder: BookmarkFolder
    )

    override suspend fun doWork(params: Params): Unit = withContext(coroutineDispatcher) {
        bookmarkFolderRepo.editFolder(params.bookmarkFolder)
    }
}
