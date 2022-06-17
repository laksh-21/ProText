package better.text.protext.ui_copiedtexts.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import better.text.protext.base.baseScreens.BaseBottomSheetDialog
import better.text.protext.base.utils.Validators
import better.text.protext.base.utils.getFromClipboard
import better.text.protext.ui_copiedtexts.R
import better.text.protext.ui_copiedtexts.databinding.FragmentAddCopiedTextBinding
import better.text.protext.ui_copiedtexts.fragments.CopiedTextFragment
import better.text.protext.ui_copiedtexts.viewmodels.AddCopiedTextViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddCopiedTextDialogFragment :
    BaseBottomSheetDialog<FragmentAddCopiedTextBinding>() {

    private val navArgs: AddCopiedTextDialogFragmentArgs by navArgs()
    private var copiedText = ""
    private val isEdit: Boolean
        get() = (navArgs.copiedTextId != -1L)

    private val viewModel: AddCopiedTextViewModel by viewModels()

    override fun onViewCreated(binding: FragmentAddCopiedTextBinding, savedInstanceState: Bundle?) {
        initViews()
        initViewListeners()
        initObservables()
        if (isEdit) {
            initData()
        }
    }

    private fun initObservables() {
        lifecycleScope.launch {
            viewModel.copiedText.flowWithLifecycle(lifecycle).collectLatest {
                if (it.id != -1L) {
                    populateCopiedText(it.text)
                }
            }
        }
    }

    private fun initData() {
        viewModel.getCopiedText(navArgs.copiedTextId)
    }

    private fun initViewListeners() {
        binding.apply {
            inputCopiedText.doOnTextChanged { _, _, _, _ ->
                btnAction.isEnabled = shouldEnableButton()
                textInputLayout.helperText = if (
                    copiedText.isNotEmpty() &&
                    copiedText == inputCopiedText.text.toString()
                ) {
                    getString(better.text.protext.base.R.string.helper_text_from_clipboard)
                } else {
                    ""
                }
            }
            btnAction.setOnClickListener {
                val args = bundleOf().apply {
                    putString(CopiedTextFragment.KEY_TEXT_CONTENT, inputCopiedText.text.toString())
                }
                val requestKey = if (isEdit)
                    CopiedTextFragment.KEY_EDIT_TEXT
                else
                    CopiedTextFragment.KEY_ADD_TEXT
                setFragmentResult(requestKey, args)
                dismiss()
            }
        }
    }

    private fun initViews() {
        if (!isEdit) {
            copiedText = requireContext().getFromClipboard()
            populateCopiedText(copiedText)
        }
        binding.apply {
            val btnText = if (!isEdit) {
                getString(better.text.protext.base.R.string.add)
            } else {
                getString(better.text.protext.base.R.string.edit)
            }
            btnAction.text = btnText
            btnAction.isEnabled = shouldEnableButton()
            val titleText = if (!isEdit) {
                getString(R.string.add_copied_text)
            } else {
                getString(R.string.edit_copied_text)
            }
            this.titleText.text = titleText
        }
    }

    private fun shouldEnableButton(): Boolean {
        return Validators.validateText(binding.inputCopiedText.text.toString())
    }

    private fun populateCopiedText(copiedText: String) {
        binding.inputCopiedText.setText(copiedText)
        if (!isEdit && copiedText.isNotEmpty()) {
            binding.textInputLayout.helperText =
                getString(better.text.protext.base.R.string.helper_text_from_clipboard)
        }
    }

    override fun inflater(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentAddCopiedTextBinding {
        return FragmentAddCopiedTextBinding.inflate(inflater, container, false)
    }
}
