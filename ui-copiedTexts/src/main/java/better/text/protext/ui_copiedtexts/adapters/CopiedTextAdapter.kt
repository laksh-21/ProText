package better.text.protext.ui_copiedtexts.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import better.text.protext.base.baseAdapters.SelectablePagedAdapter
import better.text.protext.localdata.database.entities.CopiedText
import better.text.protext.ui_copiedtexts.databinding.CopiedTextListItemBinding

class CopiedTextAdapter(
    private val onItemClick: (CopiedText) -> Unit
) : SelectablePagedAdapter<CopiedText, Long>(diffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = CopiedTextListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CopiedTextViewHolder(binding, selectionTracker)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        item?.let {
            (holder as CopiedTextViewHolder).apply {
                this.item = it
                bind(it, onItemClick)
            }
        }
    }

    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<CopiedText>() {
            override fun areItemsTheSame(oldItem: CopiedText, newItem: CopiedText): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: CopiedText, newItem: CopiedText): Boolean {
                return oldItem == newItem
            }
        }
    }
}
