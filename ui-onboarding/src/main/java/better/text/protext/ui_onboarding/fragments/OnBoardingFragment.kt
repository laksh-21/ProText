package better.text.protext.ui_onboarding.fragments // ktlint-disable package-name

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import better.text.protext.base.baseScreens.BaseFragment
import better.text.protext.ui_onboarding.R
import better.text.protext.ui_onboarding.adapters.OnBoardingAdapter
import better.text.protext.ui_onboarding.databinding.FragmentOnBoardingBinding
import better.text.protext.ui_onboarding.utils.onBoardingItems
import better.text.protext.ui_onboarding.viewmodels.OnBoardingViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnBoardingFragment :
    BaseFragment<FragmentOnBoardingBinding>() {

    private val viewModel: OnBoardingViewModel by viewModels()

    private val onPageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            binding.backButton.isEnabled = position != 0
        }
    }

    override fun onCreateView(binding: FragmentOnBoardingBinding, savedInstanceState: Bundle?) {
        initOnBoarding()
        initViewListeners()
    }

    private fun initViewListeners() {
        binding.apply {
            nextButton.setOnClickListener {
                if (pager.currentItem != onBoardingItems.size - 1) {
                    pager.setCurrentItem(pager.currentItem + 1, true)
                } else {
                    viewModel.updateOnBoardingDone()
                    val options = NavOptions
                        .Builder()
                        .setPopUpTo(R.id.onboarding_nav_graph, true)
                        .setExitAnim(android.R.anim.fade_out)
                        .build()
                    findNavController().navigate(
                        better.text.protext.ui.bookmarks.R.id.ui_bookmarks_nav_graph,
                        bundleOf(),
                        options
                    )
                }
            }
            backButton.setOnClickListener {
                pager.setCurrentItem(pager.currentItem - 1, true)
            }
        }
    }

    private fun initOnBoarding() {
        binding.apply {
            pager.adapter = OnBoardingAdapter()
            indicator.attachTo(pager)
        }
    }

    override fun onResume() {
        super.onResume()
        binding.pager.registerOnPageChangeCallback(onPageChangeCallback)
    }

    override fun onPause() {
        super.onPause()
        binding.pager.unregisterOnPageChangeCallback(onPageChangeCallback)
    }

    override fun inflater(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentOnBoardingBinding {
        return FragmentOnBoardingBinding.inflate(inflater, container, false)
    }
}
