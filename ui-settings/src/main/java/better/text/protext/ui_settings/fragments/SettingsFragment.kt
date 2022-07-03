package better.text.protext.ui_settings.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import better.text.protext.base.baseScreens.BaseFragment
import better.text.protext.base.databinding.AccessibilityListScreenBinding
import better.text.protext.ui_settings.R
import better.text.protext.ui_settings.adapters.SettingsAdapter
import better.text.protext.ui_settings.utils.SettingItem
import better.text.protext.ui_settings.utils.generateSettingsLayout

class SettingsFragment :
    BaseFragment<AccessibilityListScreenBinding>() {

    override fun onCreateView(binding: AccessibilityListScreenBinding, savedInstanceState: Bundle?) {
        initViews()
        initSettingsList()
    }

    private fun initViews() {
        binding.apply {
            val title = getString(R.string.settings)
            tvTitle.text = title
            tvTitleCollapsed.text = title
        }
    }

    private fun initSettingsList() {
        binding.rvList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = SettingsAdapter(generateSettingsLayout()) {
                handleItemClick(it)
            }
        }
    }

    private fun handleItemClick(item: SettingItem) {
    }

    override fun inflater(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): AccessibilityListScreenBinding {
        return AccessibilityListScreenBinding.inflate(inflater, container, false)
    }
}
