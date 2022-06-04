package better.text.protext.remotedata.bookmarks

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RemoteDataInject {
    @Provides
    @Singleton
    fun providesWebsiteDao(): WebsiteDao {
        return WebsiteDao()
    }
}
