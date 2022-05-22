package better.text.protext.localdata.preferences.entities

import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import better.text.protext.localdata.preferences.utils.EditType

data class UserSettings(
    val editType: EditType,
    val defaultFolder: Int,
) {
    companion object {
        val editTypeKey = stringPreferencesKey("EDIT_TYPE")
        val defaultFolderKey = intPreferencesKey("DEFAULT_FOLDER")
    }
}
