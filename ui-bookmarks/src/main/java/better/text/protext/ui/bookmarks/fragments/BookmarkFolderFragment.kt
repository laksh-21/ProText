package better.text.protext.ui.bookmarks.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import better.text.protext.base.baseAdapters.ItemListLookup
import better.text.protext.base.baseAdapters.StableIdKeyProvider
import better.text.protext.base.baseScreens.BaseFragment
import better.text.protext.base.databinding.AccessibilityListScreenBinding
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

    override fun inflater(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): AccessibilityListScreenBinding {
        return AccessibilityListScreenBinding.inflate(inflater, container, false)
    }

    override fun onCreateView(binding: AccessibilityListScreenBinding, savedInstanceState: Bundle?) {
        initViews()
        initViewListeners()
        initFoldersList()
        initObservables()
    }

    private fun initViews() {
        binding.apply {
            tvTitle.text = getString(R.string.folders)
        }
    }

    private fun initObservables() {
        lifecycleScope.launch {
            viewModel.foldersFlow.flowWithLifecycle(lifecycle).collectLatest {
                adapter.submitData(it)
            }
        }
    }

    private fun initFoldersList() {
        binding.rvList.apply {
            val adapter = BookmarkFolderAdapter()
            this@BookmarkFolderFragment.adapter = adapter
            this.adapter = adapter
            adapter.selectionTracker = SelectionTracker.Builder(
                "folder_selection",
                this,
                StableIdKeyProvider<Long>(this),
                ItemListLookup<Long>(this),
                StorageStrategy.createLongStorage()
            ).build()
            layoutManager = StaggeredGridLayoutManager(2, RecyclerView.VERTICAL)
        }
    }

    private fun initViewListeners() {
    }
}
