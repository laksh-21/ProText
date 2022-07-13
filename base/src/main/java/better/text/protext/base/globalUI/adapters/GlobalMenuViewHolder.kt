package better.text.protext.base.globalUI.adapters

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import better.text.protext.base.databinding.GlobalMenuItemBinding
import better.text.protext.base.globalUI.utils.GlobalMenuItem

class GlobalMenuViewHolder(
    private val binding: GlobalMenuItemBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: GlobalMenuItem, onItemClick: (GlobalMenuItem) -> Unit) {
        binding.apply {
            val context = root.context
            icon.setImageDrawable(ContextCompat.getDrawable(context, item.itemIcon))
            menuTitle.text = context.getString(item.itemName)
            root.setOnClickListener {
                item.clickAction()
                onItemClick(item)
            }
        }
    }
}
