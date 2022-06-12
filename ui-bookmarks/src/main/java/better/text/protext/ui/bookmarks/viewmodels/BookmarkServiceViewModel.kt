package better.text.protext.ui.bookmarks.viewmodels

import androidx.lifecycle.viewModelScope
import better.text.protext.base.baseScreens.BaseViewModel
import better.text.protext.base.baseScreens.UIEvent
import better.text.protext.base.results.InvokeStatus
import better.text.protext.interactors.bookmarks.AddBookmarkUseCase
import better.text.protext.interactors.bookmarks.GetAllFoldersUseCase
import better.text.protext.interactors.bookmarks.GetWebsiteTitleByUrlUseCase
import better.text.protext.localdata.database.entities.Bookmark
import better.text.protext.localdata.database.entities.BookmarkFolder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookmarkServiceViewModel @Inject constructor() : BaseViewModel() {

    private val _websiteTitle = MutableStateFlow("")
    val websiteTitle: StateFlow<String> = _websiteTitle

    private val _folders = MutableStateFlow(listOf<BookmarkFolder>())
    val folders: StateFlow<List<BookmarkFolder>> = _folders

    fun getBookmarkFolders(getAllFoldersUseCase: GetAllFoldersUseCase) {
        viewModelScope.launch {
            getAllFoldersUseCase(GetAllFoldersUseCase.Params).collect {
                when (it) {
                    is InvokeStatus.Success -> _folders.emit(it.data)
                    else -> {}
                }
            }
        }
    }

    fun getWebsiteTitle(
        getWebsiteTitleByUrlUseCase: GetWebsiteTitleByUrlUseCase,
        url: String
    ) {
        viewModelScope.launch {
            getWebsiteTitleByUrlUseCase(
                GetWebsiteTitleByUrlUseCase.Params(
                    url = url
                )
            ).collect {
                when (it) {
                    is InvokeStatus.Success -> _websiteTitle.emit(it.data)
                    is InvokeStatus.Failure -> sendEvent(UIEvent.ShowToast("Could not get title for this website"))
                    else -> {}
                }
            }
        }
    }

    fun addBookmark(
        useCase: AddBookmarkUseCase,
        position: Int,
        title: String,
        url: String,
    ) {
        if (position == -1) return
        val bookmark = Bookmark(
            id = 0L,
            bookmarkTitle = title,
            bookmarkUrl = url,
            bookmarkFolderId = _folders.value[position].id
        )
        viewModelScope.launch {
            useCase(
                AddBookmarkUseCase.Params(bookmark)
            ).collect {
                when (it) {
                    is InvokeStatus.Failure -> sendEvent(UIEvent.ShowToast("Something went wrong"))
                    is InvokeStatus.Success -> {
                        sendEvent(UIEvent.ShowToast("Bookmark added successfully"))
                        sendEvent(UIEvent.GoBack)
                    }
                    else -> {}
                }
            }
        }
    }
}
