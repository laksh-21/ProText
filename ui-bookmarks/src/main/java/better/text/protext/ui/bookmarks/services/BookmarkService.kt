package better.text.protext.ui.bookmarks.services

import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.*
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import better.text.protext.base.baseScreens.BaseForegroundWindowService
import better.text.protext.base.baseScreens.UIEvent
import better.text.protext.base.utils.Validators
import better.text.protext.interactors.bookmarks.AddBookmarkUseCase
import better.text.protext.interactors.bookmarks.GetAllFoldersUseCase
import better.text.protext.interactors.bookmarks.GetWebsiteTitleByUrlUseCase
import better.text.protext.localdata.database.entities.BookmarkFolder
import better.text.protext.ui.bookmarks.R
import better.text.protext.ui.bookmarks.databinding.AddBookmarkServiceLayoutBinding
import better.text.protext.ui.bookmarks.viewmodels.BookmarkServiceViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class BookmarkService : BaseForegroundWindowService<AddBookmarkServiceLayoutBinding>(
    notificationChannelId = "bookmarks_channel_id",
    notificationChannelName = "Bookmarks",
    notificationTitle = "Add a bookmark",
    notificationText = "",
    notificationIcon = null,
    windowGravity = Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL,
    windowAnimations = android.R.style.Animation_InputMethod,
    action = {}
) {
    @Inject
    lateinit var getAllFoldersUseCase: GetAllFoldersUseCase
    @Inject
    lateinit var getWebsiteTitleByUrlUseCase: GetWebsiteTitleByUrlUseCase
    @Inject
    lateinit var addBookmarkUseCase: AddBookmarkUseCase

    override val windowManager: WindowManager
        get() = getSystemService(WINDOW_SERVICE) as WindowManager

    private val viewModel: BookmarkServiceViewModel by lazy {
        BookmarkServiceViewModel()
    }

    private var websiteUrl: String = ""
    private var selectionPosition = -1

    override fun inflater(inflater: LayoutInflater): AddBookmarkServiceLayoutBinding {
        return AddBookmarkServiceLayoutBinding.inflate(inflater)
    }

    override fun onCreate(binding: AddBookmarkServiceLayoutBinding) {
        windowManager.addView(binding.root, layoutParams)
        startForeground(1, createNotification())
        initVariables()
        initViews()
        initViewListeners()
        initObservables()
        initData()
    }

    private fun initViewListeners() {
        binding.apply {
            inputBookmarkUrl.doOnTextChanged { _, _, _, _ ->
                button.isEnabled = shouldEnableButton()
                validateUrl()
            }
            inputBookmarkTitle.doOnTextChanged { _, _, _, _ ->
                button.isEnabled = shouldEnableButton()
            }

            folderNameSpinner.doOnTextChanged { _, _, _, _ ->
                button.isEnabled = shouldEnableButton()
            }
            folderNameSpinner.setOnItemClickListener { _, _, position, _ ->
                Log.d("Protext", "Selected position: $position")
                selectionPosition = position
            }
            button.setOnClickListener {
                viewModel.addBookmark(
                    useCase = addBookmarkUseCase,
                    position = selectionPosition,
                    title = binding.inputBookmarkTitle.text.toString(),
                    url = binding.inputBookmarkUrl.text.toString()
                )
            }
        }
    }

    private fun validateUrl() {
        binding.inputBookmarkUrl.apply {
            val text = this.text.toString()
            val errorText = if (Validators.validateUrl(text)) {
                ""
            } else {
                getString(better.text.protext.base.R.string.invalid_link)
            }
            binding.inputLayoutUrl.error = errorText
        }
    }

    private fun shouldEnableButton(): Boolean {
        return (
            Validators.validateUrl(binding.inputBookmarkUrl.text.toString()) &&
                Validators.validateText(binding.inputBookmarkTitle.text.toString()) &&
                Validators.validateText(binding.folderNameSpinner.text.toString())
            ).also {
            Log.d("ProtextButton", it.toString())
        }
    }

    private fun initViews() {
        binding.apply {
            inputBookmarkUrl.setText(websiteUrl)
            button.isEnabled = shouldEnableButton()
        }
    }

    private fun initVariables() {
        websiteUrl = intent?.getStringExtra(URL_EXTRA_NAME) ?: ""
    }

    private fun initObservables() {
        lifecycleScope.launch {
            viewModel.folders.flowWithLifecycle(lifecycle).collectLatest {
                Log.d("Protext", it.toString())
                initSpinner(it)
            }
        }
        lifecycleScope.launch {
            viewModel.websiteTitle.flowWithLifecycle(lifecycle).collectLatest {
                setWebsiteTitle(it)
            }
        }
        lifecycleScope.launch {
            viewModel.eventsFlow.flowWithLifecycle(lifecycle).collectLatest {
                when (it) {
                    is UIEvent.ShowToast -> Toast.makeText(this@BookmarkService, it.message, it.duration).show()
                    is UIEvent.GoBack -> close()
                    else -> {}
                }
            }
        }
    }

    private fun setWebsiteTitle(websiteTitle: String) {
        binding.inputBookmarkTitle.setText(websiteTitle)
    }

    private fun initData() {
        viewModel.getBookmarkFolders(getAllFoldersUseCase)
        if (websiteUrl.isNotEmpty()) {
            viewModel.getWebsiteTitle(getWebsiteTitleByUrlUseCase, websiteUrl)
        }
    }

    private fun initSpinner(items: List<BookmarkFolder>) {
        if (items.isEmpty()) return
        binding.folderNameSpinner.apply {
            setAdapter(
                ArrayAdapter(
                    this@BookmarkService,
                    R.layout.spinner_item_layout,
                    items.map { it.folderName }
                )
            )
        }
    }

    companion object {
        const val URL_EXTRA_NAME = "URL_EXTRA_NAME"
    }
}
