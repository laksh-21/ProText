package better.text.protext.ui_settings.utils

data class SettingsSection(
    val title: SettingItem.Heading,
    val components: List<SettingItem>
)

fun List<SettingsSection>.normalize(): List<SettingItem> {
    val result = mutableListOf<SettingItem>()
    this.map { section ->
        result.add(section.title)
        result.addAll(section.components)
    }
    return result
}
