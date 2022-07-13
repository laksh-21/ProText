package better.text.protext.ui_settings.fragments // ktlint-disable package-name

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
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
import better.text.protext.ui_settings.utils.TutorialType
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
            root.apply {
                getConstraintSet(better.text.protext.base.R.id.none).apply {
                    setVisibility(better.text.protext.base.R.id.included_menu_list, View.INVISIBLE)
                }
                getConstraintSet(better.text.protext.base.R.id.single).apply {
                    setVisibility(better.text.protext.base.R.id.included_menu_list, View.INVISIBLE)
                }
            }
            includedMenuList.root.visibility = View.INVISIBLE
        }
    }

    private fun initSettingsList(settingsState: SettingsState) {
        binding.rvList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = SettingsAdapter(
                itemsList = generateSettingsLayout(settingsState)
            ) {
                handleItemClick(it)
            }
        }
    }

    private fun handleItemClick(item: SettingItem) {
        when (item) {
            is SettingItem.Setting -> handleSettingClick(item)
            is SettingItem.Tutorial -> handleTutorialClick(item)
            else -> {}
        }
    }

    private fun handleTutorialClick(item: SettingItem.Tutorial) {
        val videoUrl = when (item.tutorialType) {
            TutorialType.CopiedText -> { COPIED_TEXT_URL }
            TutorialType.Bookmark -> { BOOKMARK_URL }
        }
        openYoutube(videoUrl)
    }

    private fun openYoutube(url: String) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(url)
        }
        startActivity(Intent.createChooser(intent, "Choose an app to open tutorial."))
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

    companion object {
        const val COPIED_TEXT_URL = "https://youtube.com/shorts/VPMqi4lbc0c?feature=share"
        const val BOOKMARK_URL = "https://youtube.com/shorts/0nOWa2s30q4?feature=share"
    }
}
