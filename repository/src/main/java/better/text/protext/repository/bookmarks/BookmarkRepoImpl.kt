package better.text.protext.repository.bookmarks

import androidx.paging.PagingSource
import better.text.protext.localdata.database.daos.BookmarkDao
import better.text.protext.localdata.database.entities.Bookmark
import javax.inject.Inject

class BookmarkRepoImpl @Inject constructor(
    private val bookmarkDao: BookmarkDao
) : BookmarkRepo {
    override suspend fun getAllBookmarks(): PagingSource<Int, Bookmark> {
        return bookmarkDao.getAll()
    }

    override suspend fun getBookmarkByFolder(folderId: Long): PagingSource<Int, Bookmark> {
        return bookmarkDao.getAllFromFolder(folderId)
    }

    override suspend fun editBookmark(bookmark: Bookmark) {
        bookmarkDao.editBookmark(bookmark)
    }

    override suspend fun deleteBookmark(bookmark: Bookmark) {
        bookmarkDao.deleteBookmark(bookmark)
    }

    override suspend fun addBookmark(bookmark: Bookmark) {
        bookmarkDao.addBookmark(bookmark)
    }
}
