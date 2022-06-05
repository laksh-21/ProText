package better.text.protext.ui.bookmarks.viewmodels

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import better.text.protext.base.baseScreens.BaseViewModel
import better.text.protext.base.baseScreens.UIEvent
import better.text.protext.base.results.InvokeStatus
import better.text.protext.interactors.bookmarks.*
import better.text.protext.localdata.database.entities.Bookmark
import better.text.protext.localdata.database.entities.BookmarkFolder
import better.text.protext.localdata.database.utils.TimeConverter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class BookmarkViewModel @Inject constructor(
    private val addBookmarkUseCase: AddBookmarkUseCase,
    private val getBookmarksByFolderUseCase: GetBookmarksByFolderUseCase,
    private val getBookmarkFolderByIdUseCase: GetBookmarkFolderByIdUseCase,
    private val deleteBookmarksUseCase: DeleteBookmarksUseCase,
    private val updateBookmarkUseCase: UpdateBookmarkUseCase
) : BaseViewModel() {

    val bookmarks: Flow<PagingData<Bookmark>> = getBookmarksByFolderUseCase.flow.cachedIn(viewModelScope)
    private val _bookmarkFolder = MutableStateFlow(
        BookmarkFolder(id = -1L, "", "")
    )
    val bookmarkFolder: StateFlow<BookmarkFolder> = _bookmarkFolder
    private var folderId: Long = -1L

    fun addBookmark(
        title: String,
        url: String
    ) {
        if (folderId == -1L) return
        viewModelScope.launch {
            addBookmarkUseCase(
                AddBookmarkUseCase.Params(
                    bookmark = Bookmark(
                        0L,
                        title,
                        url,
                        folderId
                    )
                )
            ).collect {
            }
        }
    }

    fun getBookmarks(folderId: Long) {
        this.folderId = folderId
        viewModelScope.launch {
            val pagingConfig = PagingConfig(
                pageSize = 50,
                initialLoadSize = 100,
                maxSize = 200
            )
            getBookmarksByFolderUseCase(
                GetBookmarksByFolderUseCase.Params(
                    pagingConfig = pagingConfig,
                    folderId = folderId
                )
            )
        }
    }

    fun getBookmarkFolder(folderId: Long) {
        viewModelScope.launch {
            getBookmarkFolderByIdUseCase(
                GetBookmarkFolderByIdUseCase.Params(
                    folderId = folderId
                )
            ).collect {
                when (it) {
                    is InvokeStatus.Success -> _bookmarkFolder.emit(it.data)
                    else -> {}
                }
            }
        }
    }

    fun deleteBookmarks(bookmarks: List<Long>) {
        viewModelScope.launch {
            deleteBookmarksUseCase(
                DeleteBookmarksUseCase.Params(
                    bookmarks = bookmarks
                )
            ).collect {
                when (it) {
                    is InvokeStatus.Failure -> {
                        sendEvent(UIEvent.ShowToast("Something went wrong"))
                    }
                    else -> {}
                }
            }
        }
    }

    fun updateBookmark(
        bookmarkTitle: String,
        bookmarkUrl: String,
        id: Long
    ) {
        viewModelScope.launch {
            updateBookmarkUseCase(
                UpdateBookmarkUseCase.Params(
                    Bookmark(
                        id = id,
                        bookmarkFolderId = folderId,
                        bookmarkTitle = bookmarkTitle,
                        bookmarkUrl = bookmarkUrl,
                        lastUpdated = TimeConverter().toUpdateTime(Date().time)
                    )
                )
            ).collect {
                when (it) {
                    is InvokeStatus.Failure -> {
                        sendEvent(UIEvent.ShowToast("Something went wrong"))
                        sendEvent(UIEvent.ClearSelections)
                    }
                    else -> {}
                }
            }
        }
    }
}
