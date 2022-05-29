package better.text.protext.ui.bookmarks.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
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
import better.text.protext.base.baseAdapters.StableIdKeyProvider
import better.text.protext.base.baseScreens.BaseFragment
import better.text.protext.base.databinding.AccessibilityListScreenBinding
import better.text.protext.base.utils.GridSpacingItemDecoration
import better.text.protext.ui.bookmarks.R
import better.text.protext.ui.bookmarks.adapters.BookmarkFolderAdapter
import better.text.protext.ui.bookmarks.viewmodels.BookmarkFolderViewModel
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
        }
    }

    private fun initFoldersList() {
        binding.rvList.apply {
            val adapter = BookmarkFolderAdapter()
            this@BookmarkFolderFragment.adapter = adapter
            this.adapter = adapter
            tracker = SelectionTracker.Builder(
                "folder_selection",
                binding.rvList,
                StableIdKeyProvider<Long>(binding.rvList),
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
            }
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
}
