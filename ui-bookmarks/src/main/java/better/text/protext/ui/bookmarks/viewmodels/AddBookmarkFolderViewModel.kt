package better.text.protext.ui.bookmarks.viewmodels

import androidx.lifecycle.viewModelScope
import better.text.protext.base.baseScreens.BaseViewModel
import better.text.protext.base.baseScreens.UIEvent
import better.text.protext.base.results.InvokeStatus
import better.text.protext.interactors.bookmarks.AddBookmarkFolderUseCase
import better.text.protext.interactors.bookmarks.GotBookmarkFolderByIdUseCase
import better.text.protext.interactors.bookmarks.UpdateBookmarkFolderUseCase
import better.text.protext.localdata.database.entities.BookmarkFolder
import better.text.protext.localdata.database.utils.TimeConverter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class AddBookmarkFolderViewModel @Inject constructor(
    private val addBookmarkFolderUseCase: AddBookmarkFolderUseCase,
    private val getBookmarkFolderByIdUseCase: GotBookmarkFolderByIdUseCase,
    private val updateBookmarkFolderUseCase: UpdateBookmarkFolderUseCase
) : BaseViewModel() {

    private val _folder = MutableStateFlow(
        BookmarkFolder(
            id = -1L,
            folderName = "",
            folderColor = ""
        )
    )
    val folder: StateFlow<BookmarkFolder> = _folder

    fun getPreviewColoredList(
        colorsList: List<Int>
    ): MutableList<BookmarkFolder> {
        return colorsList.mapIndexed { index: Int, color: Int ->
            BookmarkFolder(
                id = index.toLong(), // just in case diffUtils is needed in future
                folderName = "",
                folderColor = String.format("#%06X", 0xFFFFFF and color)
            )
        }.toMutableList()
    }

    fun handleAddClick(folder: BookmarkFolder) {
        if (_folder.value.id != -1L) {
            if (folder.folderColor == _folder.value.folderColor &&
                folder.folderName == _folder.value.folderName
            ) {
                sendEvent(UIEvent.GoBack)
                return
            }
            editBookmarkFolder(
                folder.copy(
                    id = _folder.value.id,
                    lastUpdated = TimeConverter().toUpdateTime(Date().time)
                )
            )
        } else {
            addBookmarkFolder(
                folder.copy(
                    id = 0L, // needed for auto-gen
                    lastUpdated = TimeConverter().toUpdateTime(Date().time)
                )
            )
        }
    }

    private fun addBookmarkFolder(folder: BookmarkFolder) {
        viewModelScope.launch {
            addBookmarkFolderUseCase(
                AddBookmarkFolderUseCase.Params(bookmarkFolder = folder)
            ).collect {
                when (it) {
                    is InvokeStatus.Success -> sendEvent(UIEvent.GoBack)
                    is InvokeStatus.Failure -> sendEvent(UIEvent.ShowToast("Something went wrong"))
                    else -> {}
                }
            }
        }
    }

    private fun editBookmarkFolder(folder: BookmarkFolder) {
        viewModelScope.launch {
            updateBookmarkFolderUseCase(
                UpdateBookmarkFolderUseCase.Params(
                    bookmarkFolder = folder
                )
            ).collect {
                when (it) {
                    is InvokeStatus.Success -> sendEvent(UIEvent.GoBack)
                    is InvokeStatus.Failure -> sendEvent(UIEvent.ShowToast("Something went wrong"))
                    else -> {}
                }
            }
        }
    }

    fun getBookmarkFolder(folderId: Long) {
        viewModelScope.launch {
            getBookmarkFolderByIdUseCase(
                GotBookmarkFolderByIdUseCase.Params(
                    folderId = folderId
                )
            ).collect {
                when (it) {
                    is InvokeStatus.Success -> _folder.emit(it.data)
                    is InvokeStatus.Failure -> sendEvent(UIEvent.ShowToast("Something went wrong"))
                    else -> {}
                }
            }
        }
    }
}
