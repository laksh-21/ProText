package better.text.protext.repository

import better.text.protext.localdata.database.daos.BookmarkDao
import better.text.protext.localdata.database.daos.BookmarkFolderDao
import better.text.protext.repository.bookmarks.BookmarkFolderRepo
import better.text.protext.repository.bookmarks.BookmarkFolderRepoImpl
import better.text.protext.repository.bookmarks.BookmarkRepo
import better.text.protext.repository.bookmarks.BookmarkRepoImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    fun provideBookmarkFolderRepo(dao: BookmarkFolderDao): BookmarkFolderRepo {
        return BookmarkFolderRepoImpl(dao)
    }
    @Provides
    fun provideBookmarkRepo(dao: BookmarkDao): BookmarkRepo {
        return BookmarkRepoImpl(dao)
    }
}
