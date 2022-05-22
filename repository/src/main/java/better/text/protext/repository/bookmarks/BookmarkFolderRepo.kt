package better.text.protext.repository.bookmarks

import androidx.paging.PagingSource
import better.text.protext.localdata.database.entities.BookmarkFolder

interface BookmarkFolderRepo {
    suspend fun getAllFolders(): PagingSource<Int, BookmarkFolder>
    suspend fun editFolder(bookmarkFolder: BookmarkFolder)
    suspend fun deleteFolder(bookmarkFolder: BookmarkFolder)
    suspend fun addFolder(bookmarkFolder: BookmarkFolder)
}
