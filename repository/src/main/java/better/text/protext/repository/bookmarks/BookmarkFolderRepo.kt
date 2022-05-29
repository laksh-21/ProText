package better.text.protext.repository.bookmarks

import androidx.paging.PagingSource
import better.text.protext.localdata.database.entities.BookmarkFolder

interface BookmarkFolderRepo {
    fun getBookmark(folderId: Long): BookmarkFolder
    fun getAllFolders(): PagingSource<Int, BookmarkFolder>
    suspend fun editFolder(bookmarkFolder: BookmarkFolder)
    suspend fun deleteFolder(bookmarkFolders: List<Long>)
    suspend fun addFolder(bookmarkFolder: BookmarkFolder)
}
