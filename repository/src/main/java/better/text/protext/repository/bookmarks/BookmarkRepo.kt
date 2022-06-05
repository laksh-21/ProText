package better.text.protext.repository.bookmarks

import androidx.paging.PagingSource
import better.text.protext.localdata.database.entities.Bookmark

interface BookmarkRepo {
    fun getAllBookmarks(): PagingSource<Int, Bookmark>
    fun getBookmarkByFolder(folderId: Long): PagingSource<Int, Bookmark>
    fun getBookmarkById(bookmarkId: Long): Bookmark
    suspend fun editBookmark(bookmark: Bookmark)
    suspend fun deleteBookmarks(bookmarks: List<Long>)
    suspend fun addBookmark(bookmark: Bookmark)
}
