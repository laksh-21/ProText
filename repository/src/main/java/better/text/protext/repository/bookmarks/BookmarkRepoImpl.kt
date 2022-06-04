package better.text.protext.repository.bookmarks

import androidx.paging.PagingSource
import better.text.protext.localdata.database.daos.BookmarkDao
import better.text.protext.localdata.database.entities.Bookmark
import javax.inject.Inject

class BookmarkRepoImpl @Inject constructor(
    private val bookmarkDao: BookmarkDao
) : BookmarkRepo {
    override fun getAllBookmarks(): PagingSource<Int, Bookmark> {
        return bookmarkDao.getAll()
    }

    override fun getBookmarkByFolder(folderId: Long): PagingSource<Int, Bookmark> {
        return bookmarkDao.getAllFromFolder(folderId)
    }

    override suspend fun editBookmark(bookmark: Bookmark) {
        bookmarkDao.editBookmark(bookmark)
    }

    override suspend fun deleteBookmarks(bookmarks: List<Long>) {
        bookmarkDao.deleteBookmarks(bookmarks)
    }

    override suspend fun addBookmark(bookmark: Bookmark) {
        bookmarkDao.addBookmark(bookmark)
    }
}
