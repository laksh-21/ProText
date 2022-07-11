package better.text.protext.ui_onboarding.adapters // ktlint-disable package-name

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import better.text.protext.ui_onboarding.databinding.OnBoardingItemBinding
import better.text.protext.ui_onboarding.utils.onBoardingItems

class OnBoardingAdapter : RecyclerView.Adapter<OnBoardingViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnBoardingViewHolder {
        return OnBoardingViewHolder(
            binding = OnBoardingItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: OnBoardingViewHolder, position: Int) {
        holder.bind(item = onBoardingItems[position])
    }

    override fun getItemCount(): Int {
        return onBoardingItems.size
    }
}
