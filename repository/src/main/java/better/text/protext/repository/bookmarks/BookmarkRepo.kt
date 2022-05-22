package better.text.protext.repository.bookmarks

import androidx.paging.PagingSource
import better.text.protext.localdata.database.entities.Bookmark

interface BookmarkRepo {
    fun getAllBookmarks(): PagingSource<Int, Bookmark>
    fun getBookmarkByFolder(folderId: Long): PagingSource<Int, Bookmark>
    suspend fun editBookmark(bookmark: Bookmark)
    suspend fun deleteBookmark(bookmark: Bookmark)
    suspend fun addBookmark(bookmark: Bookmark)
}
