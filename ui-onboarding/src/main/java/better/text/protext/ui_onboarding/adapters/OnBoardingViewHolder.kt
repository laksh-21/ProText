package better.text.protext.ui_onboarding.adapters // ktlint-disable package-name

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import better.text.protext.ui_onboarding.databinding.OnBoardingItemBinding
import better.text.protext.ui_onboarding.utils.OnBoardingItem

class OnBoardingViewHolder(
    private val binding: OnBoardingItemBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: OnBoardingItem) {
        binding.apply {
            val context = root.context
            tvOnBoardingTitle.text = context.getString(item.title)
            tvOnBoardingSubtitle.text = context.getString(item.subtitle)
            icon.setImageDrawable(ContextCompat.getDrawable(context, item.icon))
        }
    }
}
