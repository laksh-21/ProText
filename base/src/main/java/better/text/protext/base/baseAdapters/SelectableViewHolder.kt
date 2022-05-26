package better.text.protext.base.baseAdapters

import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.selection.SelectionTracker
import androidx.viewbinding.ViewBinding

abstract class SelectableViewHolder<T : Any, KT : Any>(
    binding: ViewBinding,
    private val tracker: SelectionTracker<KT>
) : BaseViewHolder<T>(binding = binding) {

    private val selectionTracker: SelectionTracker<KT>
        get() = tracker

    abstract val itemId: KT?
    lateinit var item: T

    fun getItemDetails() = object : ItemDetailsLookup.ItemDetails<KT>() {
        override fun getPosition(): Int {
            return bindingAdapterPosition
        }

        override fun getSelectionKey(): KT? {
            return itemId
        }
    }
}
