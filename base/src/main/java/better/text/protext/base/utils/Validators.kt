package better.text.protext.base.utils

import android.util.Patterns

object Validators {
    fun validateUrl(url: String): Boolean {
        return (
            url.isNotEmpty() &&
                url != "null" &&
                Patterns.WEB_URL.matcher(url).matches()
            )
    }

    fun validateText(text: String): Boolean {
        return text.isNotEmpty()
    }
}
