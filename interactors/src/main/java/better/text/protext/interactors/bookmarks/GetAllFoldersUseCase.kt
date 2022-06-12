package better.text.protext.interactors.bookmarks

import better.text.protext.base.di.IODispatcher
import better.text.protext.interactors.Interactors
import better.text.protext.localdata.database.entities.BookmarkFolder
import better.text.protext.repository.bookmarks.BookmarkFolderRepo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetAllFoldersUseCase @Inject constructor(
    @IODispatcher private val coroutineDispatcher: CoroutineDispatcher,
    private val repo: BookmarkFolderRepo
) : Interactors.SuspendUseCase<GetAllFoldersUseCase.Params, List<BookmarkFolder>>() {
    object Params

    override suspend fun doWork(params: Params): List<BookmarkFolder> = withContext(coroutineDispatcher) {
        repo.getAllBookmarks()
    }
}
