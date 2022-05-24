package better.text.protext.ui.bookmarks.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import better.text.protext.base.baseScreens.BaseFragment
import better.text.protext.ui.bookmarks.databinding.FragmentBookmarkFolderBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookmarkFolderFragment : BaseFragment<FragmentBookmarkFolderBinding>() {
    override fun inflater(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentBookmarkFolderBinding {
        return FragmentBookmarkFolderBinding.inflate(inflater, container, false)
    }

    override fun onCreateView(binding: FragmentBookmarkFolderBinding, savedInstanceState: Bundle?) {
    }
}
