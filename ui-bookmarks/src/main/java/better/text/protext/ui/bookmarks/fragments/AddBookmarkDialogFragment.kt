package better.text.protext.ui.bookmarks.fragments

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
import better.text.protext.base.databinding.AddContentDialogBinding
import better.text.protext.base.utils.Validators
import better.text.protext.base.utils.getFromClipboard
import better.text.protext.localdata.database.entities.Bookmark
import better.text.protext.ui.bookmarks.R
import better.text.protext.ui.bookmarks.viewmodels.AddBookmarkViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddBookmarkDialogFragment :
    BaseBottomSheetDialog<AddContentDialogBinding>() {

    private val viewModel: AddBookmarkViewModel by viewModels()
    private var copiedLink: String? = null
    private val navArgs: AddBookmarkDialogFragmentArgs by navArgs()
    private val isEdit: Boolean
        get() = navArgs.bookmarkId != -1L

    override fun onViewCreated(binding: AddContentDialogBinding, savedInstanceState: Bundle?) {
        initViewListeners()
        initViews()
        initData()
        initObservers()
    }

    private fun initData() {
        if (isEdit) {
            viewModel.getBookmark(navArgs.bookmarkId)
        }
    }

    private fun initViewListeners() {
        binding.apply {
            inputFirst.doOnTextChanged { _, _, _, _ ->
                btnDone.isEnabled = shouldEnableButton()
            }
            inputSecond.doOnTextChanged { _, _, _, _ ->
                inputLayoutSecond.error = ""
                inputLayoutSecond.helperText = if (inputSecond.text.toString() != copiedLink) {
                    ""
                } else {
                    getString(better.text.protext.base.R.string.helper_text_from_clipboard)
                }
                btnDone.isEnabled = shouldEnableButton()
            }
            btnDone.setOnClickListener {
                if (validateForm()) {
                    val result = bundleOf().apply {
                        putString(
                            BookmarksFragment.RESULT_TITLE_KEY,
                            binding.inputFirst.text.toString()
                        )
                        putString(
                            BookmarksFragment.RESULT_URL_KEY,
                            binding.inputSecond.text.toString()
                        )
                    }
                    val requestKey = if (isEdit) BookmarksFragment.RESULT_REQUEST_EDIT_KEY else BookmarksFragment.RESULT_REQUEST_ADD_KEY
                    setFragmentResult(
                        requestKey = requestKey,
                        result = result
                    )
//                    if (isEdit) {
//                    } else {
//                        val result = bundleOf().apply {
//                            putString(
//                                BookmarksFragment.RESULT_TITLE_KEY,
//                                binding.inputFirst.text.toString()
//                            )
//                            putString(
//                                BookmarksFragment.RESULT_URL_KEY,
//                                binding.inputSecond.text.toString()
//                            )
//                        }
//                        setFragmentResult(
//                            requestKey = BookmarksFragment.RESULT_REQUEST_ADD_KEY,
//                            result = result
//                        )
//                    }
                    dismiss()
                }
            }
        }
    }

    private fun validateForm(): Boolean {
        val isUrlValid = Validators.validateUrl(binding.inputSecond.text.toString())
        binding.apply {
            inputLayoutSecond.error = if (isUrlValid) "" else getString(better.text.protext.base.R.string.invalid_link)
        }
        return isUrlValid
    }

    private fun shouldEnableButton(): Boolean {
        val isTitleValid = Validators.validateText(binding.inputFirst.text.toString())
        val isUrlValid = Validators.validateText(binding.inputSecond.text.toString())
        return isTitleValid && isUrlValid
    }

    private fun initObservers() {
        lifecycleScope.launch {
            viewModel.title.flowWithLifecycle(lifecycle).collectLatest {
                if (it.isNotEmpty()) {
                    binding.inputFirst.setText(it)
                }
            }
        }
        lifecycleScope.launch {
            viewModel.bookmark.flowWithLifecycle(lifecycle).collectLatest {
                if (it.id != -1L) {
                    populateData(it)
                }
            }
        }
    }

    private fun populateData(bookmark: Bookmark) {
        binding.apply {
            inputFirst.setText(bookmark.bookmarkTitle)
            inputSecond.setText(bookmark.bookmarkUrl)
        }
    }

    private fun initViews() {
        binding.apply {
            tvAddTitle.text = if (!isEdit) getString(R.string.add_a_bookmark)
            else
                getString(R.string.edit_a_bookmark)
            tvAddFirst.text = getString(better.text.protext.base.R.string.title)
            tvAddSecond.text = getString(better.text.protext.base.R.string.link)
            if (!isEdit) {
                val copiedText = requireContext().getFromClipboard()
                initUrlField(copiedText)
            }
            btnDone.text = if (!isEdit) getString(better.text.protext.base.R.string.add)
            else
                getString(better.text.protext.base.R.string.edit)
            btnDone.isEnabled = false
        }
    }

    private fun initUrlField(url: String) {
        val isValid = Validators.validateUrl(url)
        if (isValid) copiedLink = url
        binding.apply {
            val inputText = if (isValid) url else ""
            inputSecond.setText(inputText)
            val helperText = if (isValid)
                getString(better.text.protext.base.R.string.helper_text_from_clipboard)
            else ""
            inputLayoutSecond.helperText = helperText
        }
        if (isValid) {
            viewModel.getWebsiteTitle(url)
        }
    }

    override fun inflater(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): AddContentDialogBinding {
        return AddContentDialogBinding.inflate(inflater, container, false)
    }
}
