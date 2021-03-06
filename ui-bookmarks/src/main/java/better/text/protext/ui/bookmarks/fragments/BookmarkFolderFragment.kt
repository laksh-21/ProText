package better.text.protext.ui.bookmarks.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.GridLayoutManager
import better.text.protext.base.baseAdapters.ItemListLookup
import better.text.protext.base.baseScreens.BaseFragment
import better.text.protext.base.baseScreens.UIEvent
import better.text.protext.base.databinding.AccessibilityListScreenBinding
import better.text.protext.base.globalUI.utils.GlobalMenuItem
import better.text.protext.base.utils.GridSpacingItemDecoration
import better.text.protext.localdata.database.entities.BookmarkFolder
import better.text.protext.ui.bookmarks.R
import better.text.protext.ui.bookmarks.adapters.BookmarkFolderAdapter
import better.text.protext.ui.bookmarks.utils.BookmarkFolderIdKeyProvider
import better.text.protext.ui.bookmarks.viewmodels.BookmarkFolderViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BookmarkFolderFragment : BaseFragment<AccessibilityListScreenBinding>() {

    private val viewModel: BookmarkFolderViewModel by viewModels()
    private lateinit var adapter: BookmarkFolderAdapter
    private lateinit var navController: NavController
    private lateinit var tracker: SelectionTracker<Long>
    private val selectionObserver =
        object : SelectionTracker.SelectionObserver<Long>() {
            override fun onSelectionChanged() {
                super.onSelectionChanged()
                val anySelected = tracker.selection.isEmpty
                val text = if (anySelected) {
                    getString(R.string.folders)
                } else {
                    "${tracker.selection.size()} selected"
                }
                binding.tvTitle.text = text
                binding.tvTitleCollapsed.text = text
                handleMenuItems(tracker.selection.size())
            }
        }

    private val menuItems = mutableListOf(
        GlobalMenuItem(
            itemName = better.text.protext.base.R.string.edit,
            itemIcon = better.text.protext.base.R.drawable.ic_edit,
            clickAction = { editFolder() }
        ),
        GlobalMenuItem(
            itemName = better.text.protext.base.R.string.delete,
            itemIcon = better.text.protext.base.R.drawable.ic_delete,
            clickAction = { showDeleteConfirmationDialog() }
        )
//        GlobalMenuItem(
//            itemName = better.text.protext.base.R.string.copy,
//            itemIcon = better.text.protext.base.R.drawable.ic_copy,
//            clickAction = { copyFolders() }
//        ),
//        GlobalMenuItem(
//            itemName = better.text.protext.base.R.string.share,
//            itemIcon = better.text.protext.base.R.drawable.ic_share,
//            clickAction = { shareFolders() }
//        )
    )

    override fun inflater(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): AccessibilityListScreenBinding {
        return AccessibilityListScreenBinding.inflate(inflater, container, false)
    }

    override fun onCreateView(binding: AccessibilityListScreenBinding, savedInstanceState: Bundle?) {
        initVariables()
        initViews()
        initViewListeners()
        initFoldersList()
        initData()
        initObservables()
    }

    private fun initVariables() {
        navController = findNavController()
    }

    private fun initData() {
        viewModel.getData()
    }

    private fun initViews() {
        binding.apply {
            tvTitle.text = getString(R.string.folders)
            tvTitleCollapsed.text = getString(R.string.folders)
        }
    }

    private fun initObservables() {
        lifecycleScope.launch {
            viewModel.foldersFlow
                .flowWithLifecycle(lifecycle)
                .collectLatest {
                    adapter.submitData(it)
                }
            viewModel.eventsFlow
                .flowWithLifecycle(lifecycle)
                .collectLatest {
                    when (it) {
                        is UIEvent.ShowToast -> {
                            Toast.makeText(requireContext(), it.message, it.duration).show()
                        }
                        else -> {}
                    }
                }
        }
    }

    private fun initFoldersList() {
        binding.rvList.apply {
            val adapter = BookmarkFolderAdapter(
                onItemClick = {
                    handledItemClick(it)
                }
            )
            this@BookmarkFolderFragment.adapter = adapter
            this.adapter = adapter
            tracker = SelectionTracker.Builder(
                "folder_selection",
                binding.rvList,
                BookmarkFolderIdKeyProvider(binding.rvList.adapter as BookmarkFolderAdapter),
                ItemListLookup<Long>(binding.rvList),
                StorageStrategy.createLongStorage()
            ).withSelectionPredicate(
                SelectionPredicates.createSelectAnything()
            ).build()
            tracker.addObserver(selectionObserver)
            adapter.selectionTracker = tracker
            layoutManager = GridLayoutManager(requireContext(), 2).also {
                addItemDecoration(
                    GridSpacingItemDecoration(
                        spacing = requireContext()
                            .resources
                            .getDimension(better.text.protext.base.R.dimen.sideMargins)
                            .toInt(),
                        spanCount = it.spanCount
                    )
                )
            }
        }
    }

    private fun handledItemClick(bookmarkFolder: BookmarkFolder) {
        navController.navigate(
            BookmarkFolderFragmentDirections
                .actionBookmarkFolderFragmentToBookmarksFragment(
                    folderId = bookmarkFolder.id
                )
        )
    }

    private fun initViewListeners() {
        binding.apply {
            includedMenuList.apply {
                add.setOnClickListener {
                    navController.navigate(
                        BookmarkFolderFragmentDirections.actionBookmarkFolderFragmentToAddBookmarkFolderFragment()
                    )
                }
                close.setOnClickListener {
                    tracker.clearSelection()
                }
                menu.setOnClickListener {
                    val menuConverted = if (tracker.selection.size() > 1) {
                        menuItems.drop(1)
                    } else {
                        menuItems
                    }
                    showMenu(menuConverted)
                }
            }
        }
    }

    private fun copyFolders() {
    }

    private fun shareFolders() {
    }

    private fun editFolder() {
        navController.navigate(
            BookmarkFolderFragmentDirections.actionBookmarkFolderFragmentToAddBookmarkFolderFragment(
                folderId = tracker.selection.map { it }.first()
            )
        )
    }

    private fun showDeleteConfirmationDialog() {
        var folders = tracker.selection.map { it }
        if (folders.size == 1 && folders.first() == 0L) {
            Toast.makeText(requireContext(), "Cannot delete default folder", Toast.LENGTH_LONG).show()
            return
        }
        if (folders.contains(0L)) {
            folders = folders.filter { it != 0L }
        }
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.delete_folder))
            .setMessage(getString(R.string.delete_folder_warning))
            .setNegativeButton("No") { _, _ -> }
            .setPositiveButton("Yes") { _, _ ->
                viewModel.deleteFolders(folders)
            }
            .show()
    }

    private fun handleMenuItems(size: Int) {
        binding.includedMenuList.menuItemsContainer.apply {
            if (size > 0) {
                transitionToEnd()
            } else {
                transitionToStart()
            }
        }
    }
}
