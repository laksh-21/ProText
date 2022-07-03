package better.text.protext.ui_settings.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import better.text.protext.ui_settings.databinding.HeadingListItemBinding
import better.text.protext.ui_settings.databinding.SettingListItemBinding
import better.text.protext.ui_settings.databinding.TutorialListItemBinding
import better.text.protext.ui_settings.utils.SettingItem

class SettingsAdapter(
    private val itemsList: List<SettingItem>,
    private val onItemClick: (SettingItem) -> Unit = {}
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            SettingItem.Heading.ID -> {
                val binding = HeadingListItemBinding.inflate(inflater, parent, false)
                HeadingViewHolder(binding)
            }
            SettingItem.Tutorial.ID -> {
                val binding = TutorialListItemBinding.inflate(inflater, parent, false)
                TutorialViewHolder(binding)
            }
            else -> {
                val binding = SettingListItemBinding.inflate(inflater, parent, false)
                SettingViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val castedHolder = when (getItemViewType(position)) {
            SettingItem.Heading.ID -> holder as HeadingViewHolder
            SettingItem.Tutorial.ID -> holder as TutorialViewHolder
            else -> holder as SettingViewHolder
        }
        castedHolder.bind(itemsList[position]) { onItemClick(it) }
    }

    override fun getItemCount(): Int = itemsList.size

    override fun getItemViewType(position: Int): Int {
        return itemsList[position].id
    }
}
