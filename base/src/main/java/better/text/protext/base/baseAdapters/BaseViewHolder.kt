package better.text.protext.base.baseAdapters

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class BaseViewHolder<T : Any, VB : ViewBinding>(
    binding: VB
) : RecyclerView.ViewHolder(binding.root) {
    abstract fun bind(item: T)
}
