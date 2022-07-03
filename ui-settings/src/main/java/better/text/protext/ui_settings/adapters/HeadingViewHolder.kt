package better.text.protext.ui_settings.adapters

import androidx.core.content.ContextCompat
import better.text.protext.base.baseAdapters.BaseViewHolder
import better.text.protext.ui_settings.databinding.HeadingListItemBinding
import better.text.protext.ui_settings.utils.SettingItem

class HeadingViewHolder(
    private val binding: HeadingListItemBinding
) : BaseViewHolder<SettingItem>(binding = binding) {
    override fun bind(item: SettingItem, onItemClick: (SettingItem) -> Unit) {
        val castedItem = (item as SettingItem.Heading)
        binding.apply {
            val text = root.context.getString(castedItem.text)
            tvSettingTitle.text = text
            settingIcon.setImageDrawable(ContextCompat.getDrawable(root.context, castedItem.icon))
        }
    }
}
