package better.text.protext.ui_settings.adapters

import better.text.protext.base.baseAdapters.BaseViewHolder
import better.text.protext.ui_settings.databinding.SettingListItemBinding
import better.text.protext.ui_settings.utils.SettingItem

class SettingViewHolder(
    private val binding: SettingListItemBinding
) : BaseViewHolder<SettingItem>(binding = binding) {
    override fun bind(item: SettingItem, onItemClick: (SettingItem) -> Unit) {
        val castedItem = (item as SettingItem.Setting)
        binding.apply {
            tvPreferenceTitle.text = root.context.getString(castedItem.title)
            tvPreferenceSubtitle.text = castedItem.subtitle
            root.setOnClickListener {
                onItemClick(item)
            }
        }
    }
}
