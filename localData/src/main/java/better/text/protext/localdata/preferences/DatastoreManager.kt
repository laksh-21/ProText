package better.text.protext.localdata.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import better.text.protext.localdata.preferences.entities.UserSettings
import better.text.protext.localdata.preferences.utils.EditType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DatastoreManager @Inject constructor(
    private val datastore: DataStore<Preferences>
) {
    val userSettingsFlow: Flow<UserSettings> = datastore.data
        .map {
            UserSettings(
                editType = EditType.getTypeFromName(it[UserSettings.editTypeKey]),
                defaultFolder = it[UserSettings.defaultFolderKey] ?: 0
            )
        }

    suspend fun updateEditType(editType: EditType) {
        datastore.edit {
            it[UserSettings.editTypeKey] = editType.name
        }
    }
    suspend fun updateDefaultFolder(defaultFolder: Int) {
        datastore.edit {
            it[UserSettings.defaultFolderKey] = defaultFolder
        }
    }
}
