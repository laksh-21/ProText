package better.text.protext.localdata.database

import android.content.Context
import androidx.room.Room
import better.text.protext.localdata.database.daos.BookmarkDao
import better.text.protext.localdata.database.daos.BookmarkFolderDao
import better.text.protext.localdata.database.daos.CopiedTextDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseInject {
    @Singleton
    @Provides
    fun providesDatabase(
        @ApplicationContext context: Context
    ): AppDatabase = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "app-database"
    ).build()

    @Singleton
    @Provides
    fun provideCopiedTextDao(db: AppDatabase): CopiedTextDao = db.copiedTextDao()
    @Singleton
    @Provides
    fun provideBookmarksDao(db: AppDatabase): BookmarkDao = db.bookmarkDao()
    @Singleton
    @Provides
    fun provideBookmarkFolderDao(db: AppDatabase): BookmarkFolderDao = db.bookmarkFolderDao()
}
