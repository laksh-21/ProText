package better.text.protext.ui_settings.adapters

import better.text.protext.base.baseAdapters.BaseViewHolder
import better.text.protext.ui_settings.databinding.TutorialListItemBinding
import better.text.protext.ui_settings.utils.SettingItem

class TutorialViewHolder(
    private val binding: TutorialListItemBinding
) : BaseViewHolder<SettingItem>(binding = binding) {
    override fun bind(item: SettingItem, onItemClick: (SettingItem) -> Unit) {
        val castedItem = (item as SettingItem.Tutorial)
        binding.apply {
            tvTutorialText.text = root.context.getString(castedItem.title)
            root.setOnClickListener {
                onItemClick(item)
            }
        }
    }
}
