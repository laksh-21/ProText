package better.text.protext.base.baseAdapters

import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.DiffUtil

abstract class SelectablePagedAdapter<T : Any, KT : Any>(
    diffCallback: DiffUtil.ItemCallback<T>
) : BasePagedAdapter<T>(diffCallback) {
    lateinit var selectionTracker: SelectionTracker<KT>
}
