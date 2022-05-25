package better.text.protext.base.baseAdapters

import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.selection.SelectionTracker
import androidx.viewbinding.ViewBinding

abstract class SelectableViewHolder<T : Any, KT : Any, VB : ViewBinding>(
    binding: VB,
    private val tracker: SelectionTracker<KT>
) : BaseViewHolder<T, VB>(binding = binding) {

    private val selectionTracker: SelectionTracker<KT>
        get() = tracker

    abstract val itemId: KT?
    abstract fun getItemDetails(): ItemDetailsLookup.ItemDetails<KT>
}
