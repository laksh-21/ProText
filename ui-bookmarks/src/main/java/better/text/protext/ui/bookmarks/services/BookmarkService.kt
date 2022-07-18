package better.text.protext.ui.bookmarks.services

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.WindowManager
import android.widget.*
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import better.text.protext.base.baseScreens.BaseForegroundWindowService
import better.text.protext.base.baseScreens.UIEvent
import better.text.protext.base.utils.NotificationData
import better.text.protext.base.utils.Validators
import better.text.protext.interactors.bookmarks.AddBookmarkUseCase
import better.text.protext.interactors.bookmarks.GetAllFoldersUseCase
import better.text.protext.interactors.bookmarks.GetWebsiteTitleByUrlUseCase
import better.text.protext.preferecnes.DatastoreManager
import better.text.protext.ui.bookmarks.R
import better.text.protext.ui.bookmarks.databinding.AddBookmarkServiceLayoutBinding
import better.text.protext.ui.bookmarks.viewmodels.BookmarkFoldersState
import better.text.protext.ui.bookmarks.viewmodels.BookmarkServiceViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class BookmarkService : BaseForegroundWindowService<AddBookmarkServiceLayoutBinding>(
    windowGravity = Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL,
    windowAnimations = android.R.style.Animation_InputMethod
) {
    @Inject
    lateinit var getAllFoldersUseCase: GetAllFoldersUseCase
    @Inject
    lateinit var getWebsiteTitleByUrlUseCase: GetWebsiteTitleByUrlUseCase
    @Inject
    lateinit var addBookmarkUseCase: AddBookmarkUseCase
    @Inject
    lateinit var datastoreManager: DatastoreManager

    override val windowManager: WindowManager
        get() = getSystemService(WINDOW_SERVICE) as WindowManager

    private val viewModel: BookmarkServiceViewModel by lazy {
        BookmarkServiceViewModel(
            getAllFoldersUseCase,
            getWebsiteTitleByUrlUseCase,
            addBookmarkUseCase,
            datastoreManager
        )
    }

    private var websiteUrl: String = ""
    private var selectionPosition = -1

    override fun inflater(inflater: LayoutInflater): AddBookmarkServiceLayoutBinding {
        return AddBookmarkServiceLayoutBinding.inflate(inflater)
    }

    override fun onCreate(binding: AddBookmarkServiceLayoutBinding) {
        windowManager.addView(binding.root, layoutParams)
        val notificationData = NotificationData(
            title = getString(R.string.add_a_bookmark),
            content = getString(better.text.protext.base.R.string.dismiss_text),
            iconId = R.drawable.ic_bookmark,
            notificationId = 4321,
            channelId = getString(R.string.bookmark_channel_id),
            channelName = getString(R.string.bookmark_channel_name),
            channelDescription = getString(R.string.bookmark_channel_description)
        )
        startForeground(1, createNotification(notificationData))
        initObservables()
    }

    override fun onStartCommand(intent: Intent?) {
        initVariables()
        initData()
        initViews()
        initViewListeners()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initViewListeners() {
        binding.apply {
            root.setOnTouchListener { _, motionEvent ->
                when (motionEvent.action) {
                    MotionEvent.ACTION_OUTSIDE -> {
                        close()
                        true
                    }
                    else -> {
                        false
                    }
                }
            }
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
                selectionPosition = position
            }
            button.setOnClickListener {
                viewModel.addBookmark(
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
            viewModel.bookmarkFoldersState.flowWithLifecycle(lifecycle).collectLatest {
                it?.let {
                    initSpinner(it)
                }
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
        lifecycleScope.launch {
            datastoreManager.userSettingsFlow.collect()
        }
    }

    private fun setWebsiteTitle(websiteTitle: String) {
        binding.inputBookmarkTitle.setText(websiteTitle)
    }

    private fun initData() {
        viewModel.getBookmarkFolders()
        if (websiteUrl.isNotEmpty()) {
            viewModel.getWebsiteTitle(websiteUrl)
        }
    }

    private fun initSpinner(bookmarkFoldersState: BookmarkFoldersState) {
        binding.folderNameSpinner.apply {
            setAdapter(
                ArrayAdapter(
                    this@BookmarkService,
                    R.layout.spinner_item_layout,
                    bookmarkFoldersState.folders.map { it.folderName }
                )
            )
            val selection = bookmarkFoldersState.folders.first {
                it.id == bookmarkFoldersState.settings.defaultFolder
            }.folderName
            setText(selection, false)
            selectionPosition = bookmarkFoldersState.folders.indexOfFirst {
                it.id == bookmarkFoldersState.settings.defaultFolder
            }
        }
    }

    companion object {
        const val URL_EXTRA_NAME = "URL_EXTRA_NAME"
    }
}
