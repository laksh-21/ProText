package better.text.protext.ui.bookmarks.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import better.text.protext.base.baseScreens.BaseFragment
import better.text.protext.ui.bookmarks.databinding.FragmentBookmarksBinding

class BookmarksFragment :
    BaseFragment<FragmentBookmarksBinding>() {

    override fun onCreateView(binding: FragmentBookmarksBinding, savedInstanceState: Bundle?) {
    }

    override fun inflater(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentBookmarksBinding {
        return FragmentBookmarksBinding.inflate(inflater, container, false)
    }
}
