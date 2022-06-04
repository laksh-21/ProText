package better.text.protext.ui.bookmarks.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.LinearLayoutManager
import better.text.protext.base.baseAdapters.ItemListLookup
import better.text.protext.base.baseScreens.BaseFragment
import better.text.protext.base.databinding.AccessibilityBackListScreenBinding
import better.text.protext.localdata.database.entities.BookmarkFolder
import better.text.protext.ui.bookmarks.adapters.BookmarksAdapter
import better.text.protext.ui.bookmarks.utils.BookmarkIdKeyProvider
import better.text.protext.ui.bookmarks.viewmodels.BookmarkViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BookmarksFragment :
    BaseFragment<AccessibilityBackListScreenBinding>() {

    private val viewModel: BookmarkViewModel by viewModels()
    private val navArgs: BookmarksFragmentArgs by navArgs()
    private lateinit var navController: NavController
    private lateinit var adapter: BookmarksAdapter
    private lateinit var tracker: SelectionTracker<Long>
    private val selectionObserver =
        object : SelectionTracker.SelectionObserver<Long>() {
            override fun onSelectionChanged() {
                super.onSelectionChanged()
                val anySelected = tracker.selection.isEmpty
                val text = if (anySelected) {
                    viewModel.bookmarkFolder.value.folderName
                } else {
                    "${tracker.selection.size()} selected"
                }
                binding.tvTitle.text = text
                binding.tvTitleCollapsed.text = text
                handleMenuItems(tracker.selection.size())
            }
        }

    override fun onCreateView(binding: AccessibilityBackListScreenBinding, savedInstanceState: Bundle?) {
        initVariables()
        initBookmarksListView()
        initData()
        initViewListeners()
        initObservables()
    }

    private fun initVariables() {
        navController = findNavController()
    }

    private fun initViewListeners() {
        binding.apply {
            this.includedMenuList.apply {
                add.setOnClickListener {
                    navController.navigate(
                        BookmarksFragmentDirections.actionBookmarksFragmentToAddBookmarkDialogFragment()
                    )
                }
                delete.setOnClickListener {
                    val folders = tracker.selection.map { it }
                    viewModel.deleteBookmarks(folders)
                }
                close.setOnClickListener {
                    tracker.clearSelection()
                }
            }
            back.setOnClickListener {
                handleBackPressed()
            }
        }
    }

    private fun handleBackPressed() {
        navController.navigateUp()
    }

    private fun initBookmarksListView() {
        binding.rvList.apply {
            this@BookmarksFragment.adapter = BookmarksAdapter(
                onItemClick = {}
            )
            adapter = this@BookmarksFragment.adapter
            tracker = SelectionTracker.Builder(
                "bookmark_selection",
                this,
                BookmarkIdKeyProvider(adapter as BookmarksAdapter),
                ItemListLookup<Long>(this),
                StorageStrategy.createLongStorage()
            ).withSelectionPredicate(
                SelectionPredicates.createSelectAnything()
            ).build()
            tracker.addObserver(selectionObserver)
            (adapter as BookmarksAdapter).selectionTracker = tracker
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun initObservables() {
        lifecycleScope.launch {
            viewModel.bookmarks
                .flowWithLifecycle(lifecycle)
                .collectLatest {
                    adapter.submitData(it)
                }
        }
        lifecycleScope.launch {
            viewModel.bookmarkFolder
                .flowWithLifecycle(lifecycle)
                .collectLatest {
                    if (it.id != -1L) {
                        initViews(it)
                    }
                }
        }
        setFragmentResultListener(RESULT_REQUEST_KEY) { _, bundle ->
            val title = bundle.getString(RESULT_TITLE_KEY)
            val url = bundle.getString(RESULT_URL_KEY)
            if (title != null && url != null) {
                viewModel.addBookmark(
                    title = title,
                    url = url
                )
            }
        }
    }

    private fun initData() {
        if (navArgs.folderId != -1L) {
            viewModel.getBookmarkFolder(navArgs.folderId)
            viewModel.getBookmarks(navArgs.folderId)
        }
    }

    private fun initViews(bookmarkFolder: BookmarkFolder) {
        binding.apply {
            val pageTitle = bookmarkFolder.folderName
            tvTitle.text = pageTitle
            tvTitleCollapsed.text = pageTitle
        }
    }

    private fun handleMenuItems(size: Int) {
        binding.includedMenuList.menuItemsContainer.apply {
            if (size == 1) {
                transitionToState(better.text.protext.base.R.id.single)
            } else if (size > 0) {
                transitionToState(better.text.protext.base.R.id.multi)
            } else {
                transitionToState(better.text.protext.base.R.id.none)
            }
        }
    }

    override fun inflater(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): AccessibilityBackListScreenBinding {
        return AccessibilityBackListScreenBinding.inflate(inflater, container, false)
    }

    companion object {
        const val RESULT_REQUEST_KEY = "bookmark_request"
        const val RESULT_TITLE_KEY = "bookmark_request_title"
        const val RESULT_URL_KEY = "bookmark_request_url"
    }
}
