package better.text.protext.interactors.bookmarks

import better.text.protext.base.di.IODispatcher
import better.text.protext.interactors.Interactors
import better.text.protext.repository.bookmarks.BookmarkRepo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DeleteBookmarksUseCase @Inject constructor(
    @IODispatcher private val coroutineDispatcher: CoroutineDispatcher,
    private val bookmarkRepo: BookmarkRepo
) : Interactors.SuspendUseCase<DeleteBookmarksUseCase.Params, Unit>() {
    data class Params(
        val bookmarks: List<Long>
    )

    override suspend fun doWork(params: Params): Unit = withContext(coroutineDispatcher) {
        bookmarkRepo.deleteBookmarks(bookmarks = params.bookmarks)
    }
}
