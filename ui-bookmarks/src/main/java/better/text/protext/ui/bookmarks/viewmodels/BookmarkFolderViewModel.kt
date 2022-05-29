package better.text.protext.ui.bookmarks.viewmodels

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import better.text.protext.base.baseScreens.BaseViewModel
import better.text.protext.interactors.bookmarks.AddBookmarkFolderUseCase
import better.text.protext.interactors.bookmarks.GetAllBookmarkFoldersUseCase
import better.text.protext.localdata.database.entities.BookmarkFolder
import better.text.protext.localdata.database.utils.TimeConverter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class BookmarkFolderViewModel @Inject constructor(
    private val getAllBookmarkFoldersUseCase: GetAllBookmarkFoldersUseCase,
    private val addBookmarkFolderUseCase: AddBookmarkFolderUseCase
) : BaseViewModel() {
    val foldersFlow: Flow<PagingData<BookmarkFolder>> =
        getAllBookmarkFoldersUseCase.flow.cachedIn(viewModelScope)

    fun getData() {
        getAllBookmarkFoldersUseCase(
            params = GetAllBookmarkFoldersUseCase.Params(
                pagingConfig = PagingConfig(
                    pageSize = 50,
                    maxSize = 150,
                )
            )
        )
    }

    fun addFolder() {
        viewModelScope.launch {
            addBookmarkFolderUseCase(
                AddBookmarkFolderUseCase.Params(
                    BookmarkFolder(
                        folderName = "New folder",
                        folderColor = "#FFDBE2",
                        lastUpdated = TimeConverter().toUpdateTime(Date().time)
                    )
                )
            ).collect {
            }
        }
    }
}
