package better.text.protext.preferecnes

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import better.text.protext.preferecnes.entities.UserSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private val Context.datastore by preferencesDataStore("settings")

class DatastoreManager @Inject constructor(
    context: Context
) {
    private val settingsDatastore = context.datastore

    val userSettingsFlow: Flow<UserSettings> = settingsDatastore.data
        .map {
            UserSettings(
                defaultFolder = it[UserSettings.defaultFolderKey] ?: 0
            )
        }

    private val onBoardingSetting = booleanPreferencesKey("PREFERENCE_ON_BOARDING_SHOWN")
    val isOnBoardingDone: Flow<Boolean> = settingsDatastore.data
        .map {
            it[onBoardingSetting] ?: false
        }

    suspend fun updateOnBoardingDone() {
        settingsDatastore.edit {
            it[onBoardingSetting] = true
        }
    }

    suspend fun<T> updateUserSetting(key: Preferences.Key<T>, setting: T) {
        settingsDatastore.edit {
            it[key] = setting
        }
    }
}
