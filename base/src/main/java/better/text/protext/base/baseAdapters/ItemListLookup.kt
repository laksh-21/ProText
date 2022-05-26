package better.text.protext.base.baseAdapters

import android.view.MotionEvent
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.widget.RecyclerView

@Suppress("UNCHECKED_CAST")
class ItemListLookup<KT : Any>(
    private val recyclerView: RecyclerView
) : ItemDetailsLookup<KT>() {
    override fun getItemDetails(e: MotionEvent): ItemDetails<KT>? {
        val view = recyclerView.findChildViewUnder(e.x, e.y)
        return view?.let {
            (recyclerView.getChildViewHolder(view) as SelectableViewHolder<*, KT>).getItemDetails()
        }
    }
}
