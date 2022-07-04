package better.text.protext.preferecnes.entities

import androidx.datastore.preferences.core.longPreferencesKey

data class UserSettings(
    val defaultFolder: Long,
) {
    companion object {
        val defaultFolderKey = longPreferencesKey("DEFAULT_FOLDER")
    }
}
