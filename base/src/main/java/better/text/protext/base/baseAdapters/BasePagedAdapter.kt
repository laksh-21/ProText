package better.text.protext.base.baseAdapters

import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil

abstract class BasePagedAdapter<T : Any, VH : SelectableViewHolder<T, *, *>>(
    diffCallback: DiffUtil.ItemCallback<T>
) : PagingDataAdapter<T, VH>(
    diffCallback = diffCallback
)
