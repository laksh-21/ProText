package better.text.protext.ui_settings.utils

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

sealed class SettingItem(val id: Int) {
    // make sure each id is different
    data class Heading(
        @DrawableRes val icon: Int,
        @StringRes val text: Int,
    ) : SettingItem(0) {
        companion object {
            const val ID = 0
        }
    }
    data class Tutorial(
        @StringRes val title: Int,
        val tutorialType: TutorialType
    ) : SettingItem(1) {
        companion object {
            const val ID = 1
        }
    }
    data class Setting(
        @StringRes val title: Int,
        val subtitle: String
    ) : SettingItem(2) {
        companion object {
            const val ID = 2
        }
    }
}
