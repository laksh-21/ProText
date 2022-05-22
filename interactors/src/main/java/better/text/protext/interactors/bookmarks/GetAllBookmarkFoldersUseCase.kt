package better.text.protext.interactors.bookmarks

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import better.text.protext.interactors.Interactors
import better.text.protext.localdata.database.entities.BookmarkFolder
import better.text.protext.repository.bookmarks.BookmarkFolderRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllBookmarkFoldersUseCase @Inject constructor(
    private val bookmarkFolderRepo: BookmarkFolderRepo
) : Interactors.PagingObserveUseCase<GetAllBookmarkFoldersUseCase.Params, BookmarkFolder>() {
    data class Params(
        override val pagingConfig: PagingConfig
    ) : Interactors.PagingObserveUseCase.Params

    override suspend fun createObservable(params: Params): Flow<PagingData<BookmarkFolder>> {
        return Pager(config = params.pagingConfig) {
            bookmarkFolderRepo.getAllFolders()
        }.flow
    }
}
