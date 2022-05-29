package better.text.protext.ui.bookmarks.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import better.text.protext.localdata.database.entities.BookmarkFolder
import better.text.protext.ui.bookmarks.databinding.BookmarkFolderGridItemBinding

class ColorSelectorAdapter(
    private val itemList: MutableList<BookmarkFolder> = mutableListOf()
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ColorSelectorViewHolder(
            LayoutInflater.from(parent.context).run {
                BookmarkFolderGridItemBinding.inflate(this, parent, false)
            }
        )
    }

    fun changeItem(position: Int, name: String) {
        itemList[position] = itemList[position].copy(
            folderName = name
        )
        notifyItemChanged(position)
    }

    fun getItem(position: Int): BookmarkFolder {
        return itemList[position]
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ColorSelectorViewHolder).bind(itemList[position])
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}
