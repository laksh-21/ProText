package better.text.protext.ui.bookmarks.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import better.text.protext.base.baseAdapters.SelectablePagedAdapter
import better.text.protext.localdata.database.entities.Bookmark
import better.text.protext.ui.bookmarks.databinding.BookmarkListItemBinding

class BookmarksAdapter(
    private val onItemClick: (Bookmark) -> Unit
) : SelectablePagedAdapter<Bookmark, Long>(
    diffCallback = differCallback
) {

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        item?.let {
            (holder as BookmarksViewHolder).apply {
                this.item = item
                bind(item, onItemClick)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return BookmarksViewHolder(
            binding = BookmarkListItemBinding.inflate(layoutInflater, parent, false),
            tracker = selectionTracker
        )
    }

    companion object {
        val differCallback = object : DiffUtil.ItemCallback<Bookmark>() {
            override fun areItemsTheSame(oldItem: Bookmark, newItem: Bookmark): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Bookmark, newItem: Bookmark): Boolean {
                return oldItem == newItem
            }
        }
    }
}
