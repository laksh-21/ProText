package better.text.protext.base.baseAdapters

import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.DiffUtil

abstract class SelectablePagedAdapter<T : Any, KT : Any, VH : SelectableViewHolder<T, KT, *>>(
    diffCallback: DiffUtil.ItemCallback<T>
) : BasePagedAdapter<T, VH>(diffCallback) {
    abstract val selectionTracker: SelectionTracker<KT>
}
