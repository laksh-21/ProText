package better.text.protext.repository.settings

import better.text.protext.localdata.preferences.DatastoreManager
import better.text.protext.localdata.preferences.entities.UserSettings
import better.text.protext.localdata.preferences.utils.EditType
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SettingsRepoImpl @Inject constructor(
    private val settingsManager: DatastoreManager
) : SettingsRepo {
    override suspend fun updateEditType(editType: EditType) {
        settingsManager.updateEditType(editType)
    }

    override suspend fun updateDefaultFolder(defaultFolder: Int) {
        settingsManager.updateDefaultFolder(defaultFolder)
    }

    override val userSettings: Flow<UserSettings>
        get() = settingsManager.userSettingsFlow
}
