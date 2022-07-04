package better.text.protext.preferecnes

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PreferencesInject {
    @Singleton
    @Provides
    fun providesDatastoreManager(@ApplicationContext applicationContext: Context): DatastoreManager {
        return DatastoreManager(applicationContext)
    }
}
