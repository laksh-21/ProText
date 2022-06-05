package better.text.protext.ui.bookmarks.viewmodels

import androidx.lifecycle.viewModelScope
import better.text.protext.base.baseScreens.BaseViewModel
import better.text.protext.base.results.InvokeStatus
import better.text.protext.interactors.bookmarks.GetBookmarkByIdUseCase
import better.text.protext.interactors.bookmarks.GetWebsiteTitleByUrlUseCase
import better.text.protext.localdata.database.entities.Bookmark
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddBookmarkViewModel @Inject constructor(
    private val getWebsiteTitleByUrlUseCase: GetWebsiteTitleByUrlUseCase,
    private val getBookmarkByIdUseCase: GetBookmarkByIdUseCase,
) : BaseViewModel() {
    private val _title: MutableStateFlow<String> = MutableStateFlow("")
    val title: StateFlow<String> = _title
    private val _bookmark: MutableStateFlow<Bookmark> = MutableStateFlow(
        Bookmark(
            id = -1L,
            bookmarkTitle = "",
            bookmarkFolderId = 0L,
            bookmarkUrl = ""
        )
    )
    val bookmark: StateFlow<Bookmark> = _bookmark

    fun getWebsiteTitle(url: String) {
        viewModelScope.launch {
            getWebsiteTitleByUrlUseCase(
                GetWebsiteTitleByUrlUseCase.Params(
                    url = url
                )
            ).collect {
                when (it) {
                    is InvokeStatus.Success -> _title.emit(it.data)
                    else -> {}
                }
            }
        }
    }

    fun getBookmark(bookmarkId: Long) {
        viewModelScope.launch {
            getBookmarkByIdUseCase(
                GetBookmarkByIdUseCase.Params(
                    bookmarkId = bookmarkId
                )
            ).collect {
                when (it) {
                    is InvokeStatus.Success -> _bookmark.emit(it.data)
                    else -> {}
                }
            }
        }
    }
}
