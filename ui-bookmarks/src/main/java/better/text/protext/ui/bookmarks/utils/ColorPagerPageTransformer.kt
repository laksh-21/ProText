package better.text.protext.ui.bookmarks.utils

import android.view.View
import androidx.viewpager2.widget.ViewPager2
import kotlin.math.abs

class ColorPagerPageTransformer(
    private val offScreenItemVisible: Float = 15f,
    private val currentItemMargin: Float = 15f,
) : ViewPager2.PageTransformer {
    override fun transformPage(page: View, position: Float) {
        val pageTranslationX = currentItemMargin + offScreenItemVisible
        page.translationX = -pageTranslationX * position
        page.scaleY = 1 - (.25f * abs(position))
    }
}
