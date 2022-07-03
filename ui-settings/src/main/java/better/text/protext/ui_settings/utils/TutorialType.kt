package better.text.protext.ui_settings.utils

sealed class TutorialType(val id: Int) {
    object CopiedText : TutorialType(0)
    object Bookmark : TutorialType(1)
}
