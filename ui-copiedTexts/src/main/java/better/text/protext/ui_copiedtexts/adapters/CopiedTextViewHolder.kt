package better.text.protext.ui_copiedtexts.adapters

import androidx.recyclerview.selection.SelectionTracker
import better.text.protext.base.baseAdapters.SelectableViewHolder
import better.text.protext.base.utils.TimeHelpers
import better.text.protext.localdata.database.entities.CopiedText
import better.text.protext.ui_copiedtexts.databinding.CopiedTextListItemBinding

class CopiedTextViewHolder(
    private val binding: CopiedTextListItemBinding,
    private val selectionTracker: SelectionTracker<Long>
) : SelectableViewHolder<CopiedText, Long>(
    binding = binding,
    tracker = selectionTracker
) {

    private var isCollapsed = true
    private val expandedLines = Int.MAX_VALUE
    private val collapsedLines = 4

    override fun bind(item: CopiedText, onItemClick: (CopiedText) -> Unit) {
        binding.apply {
            tvTime.text = TimeHelpers.getTimeString(item.lastUpdated)
            tvContent.text = item.text
            itemCard.setOnClickListener {
                if (tvContent.lineCount >= 4) {
                    handleCollapsableText()
                }
            }
            itemCard.isChecked = selectionTracker.isSelected(itemId)
        }
    }

    private fun handleCollapsableText() {
        if (isCollapsed) {
            // expand here
            binding.tvContent.maxLines = expandedLines
        } else {
            // collapse here
            binding.tvContent.maxLines = collapsedLines
        }
        isCollapsed = !isCollapsed
    }

    override val itemId: Long
        get() = item.id
}
