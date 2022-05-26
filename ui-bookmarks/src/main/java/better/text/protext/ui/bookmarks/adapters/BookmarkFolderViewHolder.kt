package better.text.protext.ui.bookmarks.adapters

import androidx.recyclerview.selection.SelectionTracker
import better.text.protext.base.baseAdapters.SelectableViewHolder
import better.text.protext.localdata.database.entities.BookmarkFolder
import better.text.protext.ui.bookmarks.databinding.BookmarkFolderGridItemBinding

class BookmarkFolderViewHolder(
    private val viewBinding: BookmarkFolderGridItemBinding,
    private val selectionTracker: SelectionTracker<Long>,
) : SelectableViewHolder<BookmarkFolder, Long>(
    binding = viewBinding,
    tracker = selectionTracker
) {
    override fun bind(item: BookmarkFolder) {
        viewBinding.apply {
            tvFolderName.text = item.folderName
        }
    }

    override val itemId: Long
        get() = item.id
}
