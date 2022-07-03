package better.text.protext.ui_settings.utils

import better.text.protext.ui_settings.R

internal fun generateSettingsLayout(): List<SettingItem> {
    return listOf(
        SettingsSection(
            title = SettingItem.Heading(
                R.drawable.ic_help,
                R.string.tutorial
            ),
            components = listOf(
                SettingItem.Tutorial(
                    title = R.string.copy_texts,
                    tutorialType = TutorialType.CopiedText
                ),
                SettingItem.Tutorial(
                    title = R.string.add_bookmarks,
                    tutorialType = TutorialType.Bookmark
                )
            )
        ),
        SettingsSection(
            title = SettingItem.Heading(
                R.drawable.ic_settings,
                R.string.bookmark_prefs
            ),
            components = listOf(
                SettingItem.Setting(
                    title = R.string.default_folder,
                    subtitle = "Deff"
                )
            )
        )
    ).normalize()
}
