package better.text.protext.ui.bookmarks.adapters

import android.graphics.Color
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
    override fun bind(
        item: BookmarkFolder,
        onItemClick: (BookmarkFolder) -> Unit
    ) {
        viewBinding.apply {
            folderCard.setCardBackgroundColor(Color.parseColor(item.folderColor))
            tvFolderName.text = item.folderName
            folderCard.isChecked = selectionTracker.isSelected(itemId)
            folderCard.setOnClickListener {
                onItemClick(item)
            }
        }
    }

    override val itemId: Long
        get() = item.id
}
