package better.text.protext.ui.bookmarks.adapters

import androidx.recyclerview.selection.SelectionTracker
import better.text.protext.base.baseAdapters.SelectableViewHolder
import better.text.protext.localdata.database.entities.Bookmark
import better.text.protext.ui.bookmarks.databinding.BookmarkListItemBinding

class BookmarksViewHolder(
    private val binding: BookmarkListItemBinding,
    private val tracker: SelectionTracker<Long>
) : SelectableViewHolder<Bookmark, Long>(
    binding = binding,
    tracker = tracker
) {
    override val itemId: Long
        get() = item.id

    override fun bind(item: Bookmark, onItemClick: (Bookmark) -> Unit) {
        binding.apply {
            tvBookmarkTitle.text = item.bookmarkTitle
            tvLink.text = item.bookmarkUrl
            tvTime.text = item.lastUpdated.stringDate
            itemCard.setOnClickListener {
                onItemClick(item)
            }
            itemCard.isChecked = tracker.isSelected(itemId)
        }
    }
}
