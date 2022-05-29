package better.text.protext.base.utils

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class LinearSpacingItemDecoration(
    private val orientation: Int = RecyclerView.VERTICAL,
    private val spacing: Int
) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        when (orientation) {
            RecyclerView.VERTICAL -> {
            }
            RecyclerView.HORIZONTAL -> {
                outRect.left = spacing
                outRect.right = spacing
            }
        }
    }
}
