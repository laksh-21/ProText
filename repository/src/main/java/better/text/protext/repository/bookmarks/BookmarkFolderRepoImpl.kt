package better.text.protext.repository.bookmarks

import androidx.paging.PagingSource
import better.text.protext.localdata.database.daos.BookmarkFolderDao
import better.text.protext.localdata.database.entities.BookmarkFolder
import javax.inject.Inject

class BookmarkFolderRepoImpl @Inject constructor(
    private val bookmarkFolderDao: BookmarkFolderDao
) : BookmarkFolderRepo {
    override fun getBookmark(folderId: Long): BookmarkFolder {
        return bookmarkFolderDao.get(id = folderId)
    }

    override fun getAllFolders(): PagingSource<Int, BookmarkFolder> {
        return bookmarkFolderDao.getAll()
    }

    override suspend fun editFolder(bookmarkFolder: BookmarkFolder) {
        bookmarkFolderDao.editFolder(bookmarkFolder)
    }

    override suspend fun deleteFolder(bookmarkFolders: List<Long>) {
        bookmarkFolderDao.deleteFolder(bookmarkFolders)
    }

    override suspend fun addFolder(bookmarkFolder: BookmarkFolder) {
        bookmarkFolderDao.addNewFolder(bookmarkFolder)
    }
}
