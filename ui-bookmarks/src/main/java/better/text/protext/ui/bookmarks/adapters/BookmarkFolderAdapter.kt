package better.text.protext.ui.bookmarks.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import better.text.protext.base.baseAdapters.SelectablePagedAdapter
import better.text.protext.localdata.database.entities.BookmarkFolder
import better.text.protext.ui.bookmarks.databinding.BookmarkFolderGridItemBinding

class BookmarkFolderAdapter(
    private val onItemClick: (BookmarkFolder) -> Unit
) : SelectablePagedAdapter<BookmarkFolder, Long>(diffCallback) {
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        item?.let {
            (holder as BookmarkFolderViewHolder).apply {
                this.item = item
                bind(item, onItemClick)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = BookmarkFolderGridItemBinding.inflate(inflater, parent, false)
        return BookmarkFolderViewHolder(binding, selectionTracker)
    }

    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<BookmarkFolder>() {
            override fun areItemsTheSame(
                oldItem: BookmarkFolder,
                newItem: BookmarkFolder
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: BookmarkFolder,
                newItem: BookmarkFolder
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}
