package better.text.protext.repository.bookmarks

import androidx.paging.PagingSource
import better.text.protext.localdata.database.daos.BookmarkFolderDao
import better.text.protext.localdata.database.entities.BookmarkFolder
import javax.inject.Inject

class BookmarkFolderRepoImpl @Inject constructor(
    private val bookmarkFolderDao: BookmarkFolderDao
) : BookmarkFolderRepo {
    override suspend fun getAllFolders(): PagingSource<Int, BookmarkFolder> {
        return bookmarkFolderDao.getAll()
    }

    override suspend fun editFolder(bookmarkFolder: BookmarkFolder) {
        bookmarkFolderDao.editFolder(bookmarkFolder)
    }

    override suspend fun deleteFolder(bookmarkFolder: BookmarkFolder) {
        bookmarkFolderDao.deleteFolder(bookmarkFolder)
    }

    override suspend fun addFolder(bookmarkFolder: BookmarkFolder) {
        bookmarkFolderDao.addNewFolder(bookmarkFolder)
    }
}
