package better.text.protext.repository.settings

import better.text.protext.localdata.preferences.entities.UserSettings
import better.text.protext.localdata.preferences.utils.EditType
import kotlinx.coroutines.flow.Flow

interface SettingsRepo {
    suspend fun updateEditType(editType: EditType)
    suspend fun updateDefaultFolder(defaultFolder: Int)
    val userSettings: Flow<UserSettings>
}
