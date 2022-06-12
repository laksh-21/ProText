package better.text.protext.repository.bookmarks

import androidx.paging.PagingSource
import better.text.protext.localdata.database.entities.BookmarkFolder

interface BookmarkFolderRepo {
    suspend fun getBookmark(folderId: Long): BookmarkFolder
    fun getFolders(): PagingSource<Int, BookmarkFolder>
    suspend fun getAllBookmarks(): List<BookmarkFolder>
    suspend fun editFolder(bookmarkFolder: BookmarkFolder)
    suspend fun deleteFolder(bookmarkFolders: List<Long>)
    suspend fun addFolder(bookmarkFolder: BookmarkFolder)
}
