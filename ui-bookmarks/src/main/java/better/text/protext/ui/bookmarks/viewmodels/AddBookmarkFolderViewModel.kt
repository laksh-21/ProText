package better.text.protext.ui.bookmarks.viewmodels

import androidx.lifecycle.viewModelScope
import better.text.protext.base.baseScreens.BaseViewModel
import better.text.protext.base.baseScreens.UIEvent
import better.text.protext.base.results.InvokeStatus
import better.text.protext.interactors.bookmarks.AddBookmarkFolderUseCase
import better.text.protext.localdata.database.entities.BookmarkFolder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddBookmarkFolderViewModel @Inject constructor(
    private val addBookmarkFolderUseCase: AddBookmarkFolderUseCase
) : BaseViewModel() {
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

    fun addBookmark(folder: BookmarkFolder) {
        viewModelScope.launch {
            addBookmarkFolderUseCase(
                AddBookmarkFolderUseCase.Params(
                    bookmarkFolder = folder.copy(
                        id = 0L // needed for auto generation of primary key
                    )
                )
            ).collect {
                when (it) {
                    is InvokeStatus.Success -> sendEvent(UIEvent.GoBack)
                    else -> {}
                }
            }
        }
    }
}
