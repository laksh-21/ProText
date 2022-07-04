package better.text.protext.ui_settings.utils

sealed class SettingType(val id: Int) {
    object BookmarkDefaultFolder : SettingType(0)
}
