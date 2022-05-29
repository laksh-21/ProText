package better.text.protext.ui.bookmarks.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import better.text.protext.base.baseScreens.BaseFragment
import better.text.protext.base.baseScreens.UIEvent
import better.text.protext.base.utils.LinearSpacingItemDecoration
import better.text.protext.ui.bookmarks.R
import better.text.protext.ui.bookmarks.adapters.ColorSelectorAdapter
import better.text.protext.ui.bookmarks.databinding.FragmentAddBookmarkFolderBinding
import better.text.protext.ui.bookmarks.utils.ColorPagerPageTransformer
import better.text.protext.ui.bookmarks.viewmodels.AddBookmarkFolderViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddBookmarkFolderFragment :
    BaseFragment<FragmentAddBookmarkFolderBinding>() {

    private val viewModel: AddBookmarkFolderViewModel by viewModels()
    private lateinit var pageChangeCallback: ViewPager2.OnPageChangeCallback
    private lateinit var navController: NavController

    override fun onCreateView(binding: FragmentAddBookmarkFolderBinding, savedInstanceState: Bundle?) {
        initVariables()
        initViews()
        initViewPager()
        initViewListeners()
        initObservables()
    }

    private fun initObservables() {
        lifecycleScope.launch {
            viewModel.eventsFlow.flowWithLifecycle(lifecycle).collectLatest {
                when (it) {
                    UIEvent.GoBack -> {
                        navController.navigateUp()
                    }
                    else -> {}
                }
            }
        }
    }

    private fun initViews() {
        binding.btnAddBookmark.isEnabled = false
        binding.colorSelector.apply {
            (getChildAt(0) as? RecyclerView)?.overScrollMode = View.OVER_SCROLL_NEVER
        }
    }

    private fun initVariables() {
        pageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                (binding.colorSelector.adapter as ColorSelectorAdapter).changeItem(
                    position = position,
                    name = binding.inputFolderName.text.toString()
                )
            }
        }
        navController = findNavController()
    }

    private fun initViewListeners() {
        binding.apply {
            colorSelector.registerOnPageChangeCallback(pageChangeCallback)
            inputFolderName.doOnTextChanged { _, _, _, _ ->
                btnAddBookmark.isEnabled = (inputFolderName.text.toString().isNotEmpty())
                val adapter = colorSelector.adapter as ColorSelectorAdapter
                adapter.changeItem(colorSelector.currentItem, inputFolderName.text.toString())
            }
            btnAddBookmark.setOnClickListener {
                val currentItem = colorSelector.currentItem
                viewModel.addBookmark(
                    (colorSelector.adapter as ColorSelectorAdapter).getItem(currentItem)
                )
            }
            btnBack.setOnClickListener {
                navController.navigateUp()
            }
        }
    }

    private fun initViewPager() {
        binding.colorSelector.apply {
            val itemMargin = resources.getDimension(better.text.protext.base.R.dimen.sideMargins)
            val colorList = resources.getIntArray(R.array.folder_colors).asList()
            adapter = ColorSelectorAdapter(
                viewModel.getPreviewColoredList(colorList)
            )
            orientation = ViewPager2.ORIENTATION_HORIZONTAL
            setPageTransformer(
                ColorPagerPageTransformer(
                    offScreenItemVisible = itemMargin / 2,
                    currentItemMargin = itemMargin
                )
            )
            addItemDecoration(
                LinearSpacingItemDecoration(
                    orientation = RecyclerView.HORIZONTAL,
                    spacing = itemMargin.toInt()
                )
            )
            offscreenPageLimit = 1
        }
    }

    override fun inflater(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentAddBookmarkFolderBinding {
        return FragmentAddBookmarkFolderBinding.inflate(inflater, container, false)
    }

    override fun onStop() {
        super.onStop()
        binding.colorSelector.unregisterOnPageChangeCallback(pageChangeCallback)
    }
}
