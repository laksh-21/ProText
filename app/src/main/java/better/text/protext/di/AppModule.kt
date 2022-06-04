package better.text.protext.di

import better.text.protext.localdata.database.DatabaseInject
import better.text.protext.remotedata.bookmarks.RemoteDataInject
import better.text.protext.repository.RepositoryModule
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module(
    includes = [
        DatabaseInject::class,
        RepositoryModule::class,
        RemoteDataInject::class,
    ]
)
@InstallIn(SingletonComponent::class)
object AppModule
