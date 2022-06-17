package better.text.protext.ui_copiedtexts.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.LinearLayoutManager
import better.text.protext.base.baseAdapters.ItemListLookup
import better.text.protext.base.baseScreens.BaseFragment
import better.text.protext.base.databinding.AccessibilityListScreenBinding
import better.text.protext.ui_copiedtexts.R
import better.text.protext.ui_copiedtexts.adapters.CopiedTextAdapter
import better.text.protext.ui_copiedtexts.utils.CopiedTextItemKeyProvider
import better.text.protext.ui_copiedtexts.viewmodels.CopiedTextViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CopiedTextFragment :
    BaseFragment<AccessibilityListScreenBinding>() {

    private lateinit var navController: NavController
    private val viewModel: CopiedTextViewModel by viewModels()
    private lateinit var adapter: CopiedTextAdapter
    private lateinit var tracker: SelectionTracker<Long>
    private var editableId: Long = -1L
    private val selectionObserver =
        object : SelectionTracker.SelectionObserver<Long>() {
            override fun onSelectionChanged() {
                super.onSelectionChanged()
                val anySelected = tracker.selection.isEmpty
                val text = if (anySelected) {
                    getString(R.string.copied_text_title)
                } else {
                    "${tracker.selection.size()} selected"
                }
                binding.tvTitle.text = text
                binding.tvTitleCollapsed.text = text
                handleMenuItems(tracker.selection.size())
            }
        }

    override fun onCreateView(binding: AccessibilityListScreenBinding, savedInstanceState: Bundle?) {
        initVariables()
        initCopiedTextListView()
        initViews()
        initData()
        initObservables()
        initViewListeners()
    }

    private fun initData() {
        viewModel.getCopiedTexts()
    }

    private fun initViewListeners() {
        binding.apply {
            includedMenuList.apply {
                add.setOnClickListener {
                    navController.navigate(
                        CopiedTextFragmentDirections.actionCopiedTextFragmentToAddCopiedTextFragment()
                    )
                }
                edit.setOnClickListener {
                    val id = tracker.selection.map { it }.first()
                    editableId = id
                    navController.navigate(
                        CopiedTextFragmentDirections.actionCopiedTextFragmentToAddCopiedTextFragment(id)
                    )
                }
                delete.setOnClickListener {
                    val idList = tracker.selection.map { it }
                    viewModel.deleteCopiedTexts(idList)
                }
            }
        }
    }

    private fun initCopiedTextListView() {
        binding.rvList.apply {
            adapter = this@CopiedTextFragment.adapter
            tracker = SelectionTracker.Builder(
                "copied_text_selection",
                this,
                CopiedTextItemKeyProvider(adapter as CopiedTextAdapter),
                ItemListLookup<Long>(this),
                StorageStrategy.createLongStorage()
            ).withSelectionPredicate(
                SelectionPredicates.createSelectAnything()
            ).build()
            tracker.addObserver(selectionObserver)
            (adapter as CopiedTextAdapter).selectionTracker = tracker
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun initObservables() {
        lifecycleScope.launch {
            viewModel.copiedTextFlow.flowWithLifecycle(lifecycle).collectLatest {
                adapter.submitData(it)
            }
        }
        setFragmentResultListener(KEY_EDIT_TEXT) { _, bundle ->
            val content = bundle.getString(KEY_TEXT_CONTENT)
            if (content != null && editableId != -1L) {
                viewModel.updateCopiedText(
                    id = editableId,
                    content = content
                )
            }
        }
        setFragmentResultListener(KEY_ADD_TEXT) { _, bundle ->
            val content = bundle.getString(KEY_TEXT_CONTENT)
            if (content != null) {
                viewModel.addCopiedText(
                    content = content
                )
            }
        }
    }

    private fun initViews() {
        binding.apply {
            val titleString = getString(R.string.copied_text_title)
            tvTitle.text = titleString
            tvTitleCollapsed.text = titleString
        }
    }

    private fun initVariables() {
        navController = findNavController()
        adapter = CopiedTextAdapter { }
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
    ): AccessibilityListScreenBinding {
        return AccessibilityListScreenBinding.inflate(inflater, container, false)
    }

    companion object {
        const val KEY_EDIT_TEXT = "KEY_EDIT_TEXT"
        const val KEY_ADD_TEXT = "KEY_ADD_TEXT"
        const val KEY_TEXT_CONTENT = "KEY_TEXT_CONTENT"
    }
}
