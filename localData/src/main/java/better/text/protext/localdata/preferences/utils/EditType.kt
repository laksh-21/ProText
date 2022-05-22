package better.text.protext.localdata.preferences.utils

sealed class EditType(val name: String) {
    abstract fun convert(value: String): String

    object LowerCase : EditType("LowerCase") {
        override fun convert(value: String): String {
            return value.lowercase()
        }
    }

    object Uppercase : EditType("UpperCase") {
        override fun convert(value: String): String {
            return value.uppercase()
        }
    }

    object ToggleCase : EditType("ToggleCase") {
        override fun convert(value: String): String {
            var isCapital = false
            return value.map {
                if (isCapital) it.uppercaseChar()
                else it.lowercaseChar()
                isCapital = !isCapital
            }.toString()
        }
    }

    companion object {
        fun getTypeFromName(name: String?): EditType = when (name) {
            LowerCase.name -> LowerCase
            Uppercase.name -> Uppercase
            ToggleCase.name -> ToggleCase
            else -> LowerCase
        }
    }
}
