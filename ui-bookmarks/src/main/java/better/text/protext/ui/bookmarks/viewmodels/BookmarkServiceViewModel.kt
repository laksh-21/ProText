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
import better.text.protext.preferecnes.DatastoreManager
import better.text.protext.preferecnes.entities.UserSettings
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookmarkServiceViewModel @Inject constructor(
    private val getAllFoldersUseCase: GetAllFoldersUseCase,
    private val getWebsiteTitleByUrlUseCase: GetWebsiteTitleByUrlUseCase,
    private val addBookmarkUseCase: AddBookmarkUseCase,
    datastoreManager: DatastoreManager
) : BaseViewModel() {

    private val _websiteTitle = MutableStateFlow("")
    val websiteTitle: StateFlow<String> = _websiteTitle

    private val folders = MutableStateFlow<List<BookmarkFolder>?>(null)
    private val userSettings = datastoreManager.userSettingsFlow
    private val _bookmarkFoldersState = MutableStateFlow<BookmarkFoldersState?>(null)
    val bookmarkFoldersState: StateFlow<BookmarkFoldersState?> = _bookmarkFoldersState

    init {
        folders.combine(userSettings) { folders: List<BookmarkFolder>?, settings: UserSettings ->
            folders?.let {
                _bookmarkFoldersState.emit(
                    BookmarkFoldersState(
                        folders = folders,
                        settings = settings
                    )
                )
            }
        }.launchIn(viewModelScope)
    }

    fun getBookmarkFolders() {
        viewModelScope.launch {
            getAllFoldersUseCase(GetAllFoldersUseCase.Params).collect {
                when (it) {
                    is InvokeStatus.Success -> folders.emit(it.data)
                    else -> {}
                }
            }
        }
    }

    fun getWebsiteTitle(url: String) {
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
        position: Int,
        title: String,
        url: String,
    ) {
        if (position == -1) return
        val bookmark = Bookmark(
            id = 0L,
            bookmarkTitle = title,
            bookmarkUrl = url,
            bookmarkFolderId = folders.value!![position].id
        )
        viewModelScope.launch {
            addBookmarkUseCase(
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

data class BookmarkFoldersState(
    val folders: List<BookmarkFolder>,
    val settings: UserSettings
)
