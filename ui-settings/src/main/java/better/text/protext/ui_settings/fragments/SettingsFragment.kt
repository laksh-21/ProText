package better.text.protext.ui_settings.fragments

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.datastore.preferences.core.Preferences
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import better.text.protext.base.baseScreens.BaseFragment
import better.text.protext.base.databinding.AccessibilityListScreenBinding
import better.text.protext.preferecnes.entities.UserSettings
import better.text.protext.ui_settings.R
import better.text.protext.ui_settings.adapters.SettingsAdapter
import better.text.protext.ui_settings.utils.SettingItem
import better.text.protext.ui_settings.utils.SettingType
import better.text.protext.ui_settings.utils.generateSettingsLayout
import better.text.protext.ui_settings.viewmodels.SettingsState
import better.text.protext.ui_settings.viewmodels.SettingsViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SettingsFragment :
    BaseFragment<AccessibilityListScreenBinding>() {

    private val viewModel: SettingsViewModel by viewModels()
    private lateinit var adapter: SettingsAdapter

    override fun onCreateView(binding: AccessibilityListScreenBinding, savedInstanceState: Bundle?) {
        initViews()
        initObservables()
        initData()
    }

    private fun initData() {
        viewModel.getAllBookmarkFolders()
    }

    private fun initObservables() {
        lifecycleScope.launch {
            viewModel.settingsStateFlow.flowWithLifecycle(lifecycle).collectLatest {
                it?.let {
                    updateSettingsUI(it)
                }
            }
        }
    }

    private fun updateSettingsUI(settingsState: SettingsState) {
        if (::adapter.isInitialized) {
        } else {
            initSettingsList(settingsState)
        }
    }

    private fun initViews() {
        binding.apply {
            val title = getString(R.string.settings)
            tvTitle.text = title
            tvTitleCollapsed.text = title
        }
    }

    private fun initSettingsList(settingsState: SettingsState) {
        binding.rvList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = SettingsAdapter(
                itemsList = generateSettingsLayout(settingsState),
            ) {
                handleItemClick(it)
            }
        }
    }

    private fun handleItemClick(item: SettingItem) {
        when (item) {
            is SettingItem.Setting -> handleSettingClick(item)
            else -> {}
        }
    }

    private fun handleSettingClick(item: SettingItem.Setting) {
        when (item.settingType) {
            SettingType.BookmarkDefaultFolder -> showDefaultFolderDialog()
        }
    }

    private fun showDefaultFolderDialog() {
        val settingsState = viewModel.settingsStateFlow.value ?: return
        val foldersList = settingsState.bookmarkFolders
        val settings = settingsState.userSetting
        val selectionListener =
            DialogInterface.OnClickListener { _, position ->
                updateLongValueSetting(
                    key = UserSettings.defaultFolderKey,
                    value = foldersList[position].id
                )
            }
        val selectedIndex = if (foldersList.none { it.id == settings.defaultFolder }) {
            foldersList.indexOfFirst { it.id == 0L }
        } else {
            foldersList.indexOfFirst { it.id == settings.defaultFolder }
        }
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.default_folder)
            .setSingleChoiceItems(
                foldersList.map { it.folderName }.toTypedArray(),
                selectedIndex,
                selectionListener
            )
            .setPositiveButton("Done") { _, _ -> }
            .show()
    }

    private fun updateLongValueSetting(key: Preferences.Key<Long>, value: Long) {
        viewModel.updateSetting(key, value)
    }

    override fun inflater(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): AccessibilityListScreenBinding {
        return AccessibilityListScreenBinding.inflate(inflater, container, false)
    }
}
