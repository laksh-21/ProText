package better.text.protext.interactors.bookmarks

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import better.text.protext.interactors.Interactors
import better.text.protext.localdata.database.entities.Bookmark
import better.text.protext.repository.bookmarks.BookmarkRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetBookmarksByFolderUseCase @Inject constructor(
    private val bookmarkRepo: BookmarkRepo
) : Interactors.PagingObserveUseCase<GetBookmarksByFolderUseCase.Params, Bookmark>() {
    override suspend fun createObservable(params: Params): Flow<PagingData<Bookmark>> {
        return Pager(config = params.pagingConfig) {
            bookmarkRepo.getBookmarkByFolder(folderId = params.folderId)
        }.flow
    }
    data class Params(
        override val pagingConfig: PagingConfig,
        val folderId: Long
    ) : Interactors.PagingObserveUseCase.Params
}
