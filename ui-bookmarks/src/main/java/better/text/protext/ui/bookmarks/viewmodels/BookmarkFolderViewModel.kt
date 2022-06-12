package better.text.protext.ui.bookmarks.viewmodels

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import better.text.protext.base.baseScreens.BaseViewModel
import better.text.protext.base.baseScreens.UIEvent
import better.text.protext.base.results.InvokeStatus
import better.text.protext.interactors.bookmarks.DeleteBookmarkFoldersUseCase
import better.text.protext.interactors.bookmarks.GetBookmarkFoldersUseCase
import better.text.protext.localdata.database.entities.BookmarkFolder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookmarkFolderViewModel @Inject constructor(
    private val getBookmarkFoldersUseCase: GetBookmarkFoldersUseCase,
    private val deleteBookmarkFoldersUseCase: DeleteBookmarkFoldersUseCase
) : BaseViewModel() {
    val foldersFlow: Flow<PagingData<BookmarkFolder>> =
        getBookmarkFoldersUseCase.flow.cachedIn(viewModelScope)

    fun getData() {
        getBookmarkFoldersUseCase(
            params = GetBookmarkFoldersUseCase.Params(
                pagingConfig = PagingConfig(
                    pageSize = 50,
                    maxSize = 150,
                )
            )
        )
    }

    fun deleteFolders(bookmarkFolders: List<Long>) {
        viewModelScope.launch {
            deleteBookmarkFoldersUseCase(
                DeleteBookmarkFoldersUseCase.Params(
                    bookmarkFolders = bookmarkFolders
                )
            ).collect {
                when (it) {
                    is InvokeStatus.Failure -> sendEvent(UIEvent.ShowToast("Something went wrong."))
                    else -> {}
                }
            }
        }
    }
}
