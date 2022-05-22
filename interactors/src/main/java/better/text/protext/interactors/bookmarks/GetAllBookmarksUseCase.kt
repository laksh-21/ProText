package better.text.protext.interactors.bookmarks

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import better.text.protext.interactors.Interactors
import better.text.protext.localdata.database.entities.Bookmark
import better.text.protext.repository.bookmarks.BookmarkRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllBookmarksUseCase @Inject constructor(
    private val bookmarkRepo: BookmarkRepo
) : Interactors.PagingObserveUseCase<GetAllBookmarksUseCase.Params, Bookmark>() {
    override suspend fun createObservable(params: Params): Flow<PagingData<Bookmark>> {
        return Pager(config = params.pagingConfig) {
            bookmarkRepo.getAllBookmarks()
        }.flow
    }
    data class Params(
        override val pagingConfig: PagingConfig,
    ) : Interactors.PagingObserveUseCase.Params
}
