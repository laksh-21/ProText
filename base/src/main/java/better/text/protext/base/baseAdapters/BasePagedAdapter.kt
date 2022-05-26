package better.text.protext.base.baseAdapters

import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

abstract class BasePagedAdapter<T : Any>(
    diffCallback: DiffUtil.ItemCallback<T>
) : PagingDataAdapter<T, RecyclerView.ViewHolder>(
    diffCallback = diffCallback
)
