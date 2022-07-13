package better.text.protext.base.globalUI.utils

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class GlobalMenuItem(
    @StringRes val itemName: Int,
    @DrawableRes val itemIcon: Int,
    val clickAction: () -> Unit
)
